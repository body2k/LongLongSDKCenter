package com.longlong.common.secure.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Data
@ConfigurationPropertiesScan("spring.application")
@EnableConfigurationProperties
public class ServerName {
    private String name;
}
