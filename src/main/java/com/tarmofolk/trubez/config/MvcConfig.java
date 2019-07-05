package com.tarmofolk.trubez.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class MvcConfig implements WebMvcConfigurer {

    // чтобы раздавать картинки которые загрузил
    // ищет в пропертис директорию путь и вставляет в переменную
    @Value("${upload.path}")
    private String uploadPath;


    /** создаем метод, который позволяет общаться с внешними приложениями при помощи рест запросов (с гугл капчой) */
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }


    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
                  // каждый запрос к серверу по пути img/что нибудь
        registry.addResourceHandler("/img/**")
                  // будет перенаправлять на // файл + путь загрузки
                .addResourceLocations("file://" + uploadPath + "/");
                  // при каждом запросу к серверу по пути static/что нибудь
        registry.addResourceHandler("/static/**")
                  // ресурсы будут искаться не просто в файловой системе а в дереве проекта
                .addResourceLocations("classpath:/static/");
    }
}