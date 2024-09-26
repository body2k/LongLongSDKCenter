
package com.longlong.common.secure.config;


import com.longlong.common.secure.props.ServerName;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import com.longlong.common.secure.aspect.AuthAspect;
import com.longlong.common.secure.interceptor.ClientInterceptor;
import com.longlong.common.secure.interceptor.SecureInterceptor;
import com.longlong.common.secure.props.CloudClientProperties;
import com.longlong.common.secure.props.CloudSecureProperties;
import com.longlong.common.secure.provider.ClientDetailsServiceImpl;
import com.longlong.common.secure.provider.IClientDetailsService;
import com.longlong.common.secure.registry.SecureRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 安全配置类
 */

@Configuration
@AllArgsConstructor
@EnableConfigurationProperties({CloudSecureProperties.class, CloudClientProperties.class})
public class SecureConfiguration implements WebMvcConfigurer {
    @Resource
    private final SecureRegistry secureRegistry;

    private final CloudSecureProperties secureProperties;

    private final CloudClientProperties clientProperties;

    @Resource
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 遍历clientProperties中的client，为每个client添加ClientInterceptor拦截器，并指定拦截路径
        clientProperties.getClient().forEach(cs -> registry.addInterceptor(new ClientInterceptor(cs.getClientId())).addPathPatterns(cs.getPathPatterns()));

        // 如果secureRegistry启用，则添加SecureInterceptor拦截器，并指定排除拦截的路径
        if (secureRegistry.isEnable()) {
            registry.addInterceptor(new SecureInterceptor())
                    .excludePathPatterns(secureRegistry.getExcludePatterns())
                    .excludePathPatterns(secureRegistry.getDefaultExcludePatterns())
                    .excludePathPatterns(secureProperties.getExcludePatterns());
        }
    }

    @Bean
    public AuthAspect authAspect() {
        return new AuthAspect();
    }

    @Bean
    @ConditionalOnMissingBean(IClientDetailsService.class)
    public IClientDetailsService clientDetailsService() {
        return new ClientDetailsServiceImpl(jdbcTemplate);
    }

}
