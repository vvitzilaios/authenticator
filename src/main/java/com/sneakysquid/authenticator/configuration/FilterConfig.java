package com.sneakysquid.authenticator.configuration;


import com.sneakysquid.authenticator.filter.EnhancedHeaderFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<EnhancedHeaderFilter> addUsernameHeader() {
        FilterRegistrationBean<EnhancedHeaderFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new EnhancedHeaderFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}