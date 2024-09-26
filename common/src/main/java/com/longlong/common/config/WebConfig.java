package com.longlong.common.config;


import com.longlong.common.request.MultiRequestBodyArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer ;

import java.nio.charset.Charset;
import java.util.List;

/**
 * 添加多RequestBody解析器
 * @author 明明如月
 * @date 2018/08/27
 */
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer  {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new MultiRequestBodyArgumentResolver());
    }


}