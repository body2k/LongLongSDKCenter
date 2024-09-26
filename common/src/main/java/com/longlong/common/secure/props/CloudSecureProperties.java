
package com.longlong.common.secure.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * secure放行额外配置
 *
 * 
 */
@Data
@ConfigurationProperties("cloud.secure.url")
public class CloudSecureProperties {

	private final List<String> excludePatterns = new ArrayList<>();

}
