package com.markerhub.shiro;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.markerhub.util.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Shiro 的 JWT 过滤器：只负责“把请求头里的 Token 变成 Shiro 能用的登录凭据，并决定放行还是拦截”。
 *
 * 执行顺序（父类 AuthenticatingFilter 约定）：
 *  preHandle → isAccessAllowed 为 false 时 → onAccessDenied
 *  在 onAccessDenied 里：有 Token 且有效则 executeLogin（内部会 createToken + Realm 认证），成功则 onLoginSuccess
 */
@Component
@Slf4j
public class JwtFilter extends AuthenticatingFilter {

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 从请求里取出 Token，包装成 Shiro 的 AuthenticationToken。
     * 父类执行登录时会调用：先 createToken，再把 token 交给 Realm 做认证。
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        String jwt = ((HttpServletRequest) servletRequest).getHeader("Authorization");
        if (StringUtils.isEmpty(jwt)) {
            return null;
        }
        return new JwtToken(jwt);
    }

    /**
     * “拒绝访问时”的逻辑：当请求需要认证但还没通过时，会进到这里。
     * - 没有 Token：直接放行（return true），后续若访问了 @RequiresAuthentication 的接口，由 Shiro 再报未认证）。
     * - 有 Token：校验签名与是否过期；通过则 executeLogin（交给 Realm），失败则重定向到未授权页并 return false。
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) {
        System.out.println("onAccessDenied...");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Authorization");

        if (StringUtils.isEmpty(jwt)) {
            log.info("请求未携带 Token, URI={}, method={}", request.getRequestURI(), request.getMethod());
            responseError(servletResponse, "token已失效，请重新登录");
            return false;
        }

        try {
            Claims claim = jwtUtils.getClaimByToken(jwt);
            if (claim == null || jwtUtils.isTokenExpired(claim.getExpiration())) {
                responseError(servletResponse, "token已失效，请重新登录");
                return false;
            }
            this.executeLogin(servletRequest, servletResponse);
        } catch (Exception e) {
            if (e.getCause() instanceof SignatureVerificationException) {
                log.warn("JWT 签名校验失败: {}", e.getCause().getMessage());
            } else if (e.getCause() instanceof TokenExpiredException) {
                log.warn("JWT 已过期: {}", e.getCause().getMessage());
            } else {
                log.info("JWT 认证失败", e);
            }
            responseError(servletResponse, "token已失效，请重新登录");
            return false;
        }
        return true;
    }

    /**
     * 认证失败时统一处理：返回 401 + JSON，前端拦截器会跳转登录页，用户可执行 POST /login 重新登录。
     */
    private void responseError(ServletResponse response, String message) {
        try {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json;charset=UTF-8");
            String escaped = message.replace("\\", "\\\\").replace("\"", "\\\"");
            String json = "{\"code\":401,\"msg\":\"" + escaped + "\",\"data\":null}";
            httpResponse.getOutputStream().write(json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.warn("写入 401 响应失败: {}", e.getMessage());
        }
    }

    /**
     * 登录成功后的回调：这里直接放行即可，Subject 里已有用户信息。
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) {
        return true;
    }

    /**
     * 在 Shiro 做认证之前执行：处理 CORS 和 OPTIONS 预检。
     * - 设置跨域相关响应头；
     * - OPTIONS 请求直接返回 200，不继续走认证。
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        httpResponse.setHeader("Access-Control-Allow-Origin", httpRequest.getHeader("Origin"));
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpResponse.setHeader("Access-Control-Allow-Headers", httpRequest.getHeader("Access-Control-Request-Headers"));
        if (RequestMethod.OPTIONS.name().equals(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return false;
        }
        return super.preHandle(request, response);
    }
}
