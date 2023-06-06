package edu.ucsb.cs156.gauchoride.interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class AdminInterceptorsConfig implements WebMvcConfigurer {

    private AdminInterceptors adminInterceptors;

    @Autowired
    public AdminInterceptorsConfig(AdminInterceptors adminInterceptors) {
        this.adminInterceptors = adminInterceptors;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminInterceptors);
    }
}