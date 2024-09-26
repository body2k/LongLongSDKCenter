package com.longlong.common.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import com.longlong.common.jackson.MappingApiJackson2HttpMessageConverter;
import com.longlong.common.utils.Charsets;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.*;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 消息配置类
 */
@Configuration
@AllArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MessageConfiguration implements WebMvcConfigurer {
    @Resource
    private final ObjectMapper objectMapper;

    /**
     * 使用 JACKSON 作为JSON MessageConverter
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 移除 StringHttpMessageConverter 和 AbstractJackson2HttpMessageConverter
        converters.removeIf(x -> x instanceof StringHttpMessageConverter || x instanceof AbstractJackson2HttpMessageConverter);
        // 添加 StringHttpMessageConverter，使用 UTF-8 编码
        converters.add(new StringHttpMessageConverter(Charsets.UTF_8));
        // 添加 ByteArrayHttpMessageConverter
        converters.add(new ByteArrayHttpMessageConverter());
        // 添加 ResourceHttpMessageConverter
        converters.add(new ResourceHttpMessageConverter());
        // 添加 ResourceRegionHttpMessageConverter
        converters.add(new ResourceRegionHttpMessageConverter());
        // 添加 MappingApiJackson2HttpMessageConverter，使用自定义的 ObjectMapper
        converters.add(new MappingApiJackson2HttpMessageConverter(objectMapper));
    }

}
