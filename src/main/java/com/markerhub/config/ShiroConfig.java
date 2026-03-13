package com.markerhub.config;

import com.markerhub.shiro.AccountRealm;
import com.markerhub.shiro.JwtFilter;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro 安全配置：谁先执行、谁验 JWT、谁管权限，都在这里串起来。
 *
 * 请求进来后的顺序可以理解为：
 *  请求 → Shiro 过滤器链(根据 URL 决定 anon 还是 jwt)
 *       → 若是 jwt：JwtFilter 校验 Token → 通过则交给 AccountRealm 认人+取角色
 *       → 最后到 Controller，方法上的 @RequiresAuthentication / @RequiresRoles 由 Shiro 在进方法前校验
 */
@Configuration
public class ShiroConfig {

    @Autowired
    private JwtFilter jwtFilter;

    // ==================== ① 认证与授权的“数据源” ====================

    /**
     * AccountRealm（Realm）
     * 作用：真正“认人”和“取权限”的地方。
     * - 认人：收到 JwtToken 后，从 JWT 里取出 account，查用户表，校验通过则把用户信息放进 Subject。
     * - 取权限：根据当前用户从用户表查 permission 字段，交给 Shiro 做 @RequiresRoles 校验。
     * 可以理解为：Shiro 的“数据库 + 规则”都在这一个 Bean 里。
     */
    @Bean("accountRealm")
    public AccountRealm accountRealm() {
        return new AccountRealm();
    }

    // ==================== ② 安全大脑：统一调度 ====================

    /**
     * SecurityManager（安全管理器）
     * 作用：Shiro 的“总控”，所有认证、授权、Session 都归它管。
     * 这里只注入了 accountRealm，即：认证和授权都交给 AccountRealm（JWT + 用户表）。
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean("securityManager")
    @DependsOn("accountRealm")
    public DefaultWebSecurityManager securityManager(AccountRealm accountRealm) {
        return new DefaultWebSecurityManager(accountRealm);
    }

    /**
     * Shiro 自动配置（AbstractShiroWebFilterConfiguration）会注入此 Bean，必须提供。
     * 实际 URL 规则在下面的 shiroFilterFactoryBean 里配置，这里返回空链即可。
     */
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        return new DefaultShiroFilterChainDefinition();
    }

    // ==================== ③ 让 @RequiresRoles 等注解生效 ====================

    /**
     * DefaultAdvisorAutoProxyCreator
     * 作用：为带 @RequiresAuthentication、@RequiresRoles 的 Bean 创建代理，这样方法执行前 Shiro 才能做权限检查。
     * 没有这个 Bean，Controller 上的 @RequiresRoles("user") 不会生效。
     */
    @DependsOn("lifecycleBeanPostProcessor")
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setUsePrefix(true);
        return proxyCreator;
    }

    // ==================== ④ 过滤器链：哪些 URL 要验 JWT、哪些直接放行 ====================

    /**
     * ShiroFilterFactoryBean（Shiro 过滤器工厂）
     * 作用：定义“请求进来后先经过谁”。
     *
     * 两件事：
     * 1. 注册名为 "jwt" 的过滤器 = 我们写的 JwtFilter（从 Header 取 JWT、校验、失败重定向等）。
     * 2. URL 规则（按顺序匹配）：
     *    - anon：匿名，不验 JWT，直接放行（登录、注册、静态等）。
     *    - jwt：走 JwtFilter，没有有效 Token 会被拦下来。
     *
     * 顺序很重要：先配的优先，所以 /** 放在最后，表示“其余所有请求都要过 jwt”。
     */
    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);

        Map<String, Filter> filters = new HashMap<>();
        filters.put("jwt", jwtFilter);
        shiroFilter.setFilters(filters);

        LinkedHashMap<String, String> filterMap = new LinkedHashMap<>(16);
        filterMap.put("/login", "anon");
        filterMap.put("/register", "anon");
        filterMap.put("/**", "jwt");

        shiroFilter.setFilterChainDefinitionMap(filterMap);
        return shiroFilter;
    }
}
