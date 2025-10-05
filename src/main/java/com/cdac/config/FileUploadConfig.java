package com.cdac.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FileUploadConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve uploaded files
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
        
        // Serve images
        registry.addResourceHandler("/uploads/images/**")
                .addResourceLocations("file:uploads/images/");
        
        // Serve documents
        registry.addResourceHandler("/uploads/documents/**")
                .addResourceLocations("file:uploads/documents/");
    }
}
