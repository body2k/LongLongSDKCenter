package com.longlong.common.file;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("windows")
@Slf4j
@Data
@Aspect
@Component
public class WindowsFileProperties {

    public String file="C:\\";


}
