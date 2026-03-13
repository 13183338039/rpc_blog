package com.markerhub.shiro;

import cn.hutool.core.bean.BeanUtil;
import com.markerhub.common.Constant;
import com.markerhub.util.JwtUtils;
import com.markerhub.util.StringUtil;
import com.rpc.entity.User;
import com.rpc.example.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

/**
 * Shiro 的 Realm：只做两件事——① 用 JWT 认人（认证）② 从用户表取权限/角色（授权）。
 *
 * 请求经过 JwtFilter 后，会带着 JwtToken 进来；只有本 Realm 声明 supports(JwtToken)，
 * 所以认证和授权都会在这里完成。
 */
@Slf4j
public class AccountRealm extends AuthorizingRealm {

    @Autowired
    @Lazy
    private UserService userService;

    public AccountRealm() {
        this.setCachingEnabled(false);
    }

    /**
     * 只处理 JwtToken，其它类型的 Token（如用户名密码）不处理。
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 授权：当前用户有哪些角色。
     * 当访问带有 @RequiresRoles("user") 等注解的接口时，Shiro 会调这里，把角色塞进 Subject。
     * 这里从用户表查 permission 字段，当作唯一角色加入。
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("doGetAuthorizationInfo...");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        AccountProfile profile = (AccountProfile) principals.getPrimaryPrincipal();
        User user = userService.getAById(profile.getId());
        if (user != null && user.getPermission() != null) {
            System.out.println("user.getPermission():   "+user.getPermission());
            info.addRole(user.getPermission());
        }else{
            System.out.println("user:   "+user);
            System.out.println("user.getPermission() :   "+user.getPermission());
        }
        return info;
    }

    /**
     * 认证：这个 Token 对应的是谁、是否有效。
     * 从 JWT 里取出 account（用户名）→ 查用户表 → 用户存在且未禁用则把用户信息放进 Principal，供后续 ShiroUtil.getProfile() 使用。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("doGetAuthenticationInfo...");
        String token = (String) authenticationToken.getCredentials();
        String account = JwtUtils.getClaim(token, Constant.ACCOUNT);
        if (StringUtil.isBlank(account)) {
            throw new AuthenticationException("Token中帐号为空");
        }
        User user = userService.getAByUsername(account);
        if (user == null) {
            throw new AuthenticationException("该帐号不存在");
        }
        if (user.getStatus() != null && user.getStatus() == -1) {
            throw new AuthenticationException("帐号已禁用");
        }
        AccountProfile profile = new AccountProfile();
        BeanUtil.copyProperties(user, profile);
        return new SimpleAuthenticationInfo(profile, token, getName());
    }
}
