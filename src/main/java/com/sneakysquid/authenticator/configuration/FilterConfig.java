package com.sneakysquid.authenticator.configuration;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    /*@Bean
    public FilterRegistrationBean<EnhancedHeaderFilter> addUsernameHeader() {
        FilterRegistrationBean<EnhancedHeaderFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new EnhancedHeaderFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }*/
}