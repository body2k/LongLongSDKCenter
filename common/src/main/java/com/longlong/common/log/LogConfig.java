package com.longlong.common.log;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogConfig {
    @Value("${log.mysql.regulations}")
  static    String PATTERN_DATETIME ="yyMMdd";
    @Value("${log.mysql.is-multiple-tables}")
    static  Boolean isMultipleTables=true;
}
