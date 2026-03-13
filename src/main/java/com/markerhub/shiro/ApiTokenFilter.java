package com.markerhub.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


/**
 * 验证API接口携带的Token
 *
 * @author Administrator
 *
 */
@Component
public class ApiTokenFilter extends PathMatchingFilter {

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
//        if (!APITokenUtils.isValidToken(request)) {
//            WebUtils.writeJsonToResponse(response, JSONObject.toJSONString(JSONResultUtil.error("访问拒绝，无效token")));
//            return false;
//        }
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        System.out.println(httpServletRequest.getRequestURI()+"api-preHandle");
        return true;
    }

}
