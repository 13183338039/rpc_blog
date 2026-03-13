package com.markerhub.config;

import com.fasterxml.jackson.core.filter.TokenFilter;
import com.markerhub.shiro.ApiTokenFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.markerhub.shiro.ApiTokenFilter;
import com.markerhub.shiro.JwtFilter;
@Configuration
public class MyFilterConfig {

    @Bean
    public FilterRegistrationBean<ApiTokenFilter> tokenFilterFilterRegistrationBean(ApiTokenFilter tokenFilter){
        FilterRegistrationBean<ApiTokenFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(tokenFilter);
        // 不让该filter出现在全局过滤器链中
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<JwtFilter> tokenFilterFilterRegistrationBean1(JwtFilter tokenFilter){
        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(tokenFilter);
        // 不让该filter出现在全局过滤器链中
        registrationBean.setEnabled(false);
        return registrationBean;
    }
}


