
package com.longlong.common.secure.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户端校验配置
 *
 * 
 */
@Data
@ConfigurationProperties("cloud.secure")
public class CloudClientProperties {

	private final List<ClientSecure> client = new ArrayList<>();

}
