/*
 *Copyright © 2022 SMLOG
 *SMLOG
 *https://smlog.github.io
 *All rights reserved.
 */
package com.example.project.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.project.interceptor.LoginInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("#{'${security.exclude-uris}'.split(',')}")
    private List<String> excludeUris;
    @Autowired
    private LoginInterceptor loginInterceptor;
    
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
    	registry.addViewController("/").setViewName("forward:index.html");
    
    }
    
   /* @Override
    public void addCorsMappings(CorsRegistry registry) {
    	registry.addMapping("/**").allowedOrigins("*");
    	
    }*/
    
  //解决  No mapping for GET /favicon.ico 访问静态资源图标
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
              //  .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/static/**") .addResourceLocations("classpath:/META-INF/resources/static/");
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(loginInterceptor).addPathPatterns("/api/**");
        if (!excludeUris.isEmpty()) {
            registration.excludePathPatterns(excludeUris);
        }
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
    	return new  BCryptPasswordEncoder();
    }
    
    
    
}
