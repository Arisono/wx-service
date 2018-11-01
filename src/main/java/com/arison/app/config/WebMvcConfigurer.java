package com.arison.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter  {
	
	//解决跨域问题
    @Override
    public void addCorsMappings(CorsRegistry registry) {
    	   registry.addMapping("/**")  
           .allowCredentials(true)  
           .allowedHeaders("*")  
           .allowedOrigins("*")  
           .allowedMethods("*");  
    }
}
