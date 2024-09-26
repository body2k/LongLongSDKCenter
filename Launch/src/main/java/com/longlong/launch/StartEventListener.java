package com.longlong.launch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StringUtils;

import java.net.InetAddress;

/**
 * 项目启动事件通知
 */
@Slf4j
@Configuration
public class StartEventListener {

	@Async
	@Order
	@EventListener(WebServerInitializedEvent.class)
	// 异步执行，按照定义的顺序，@Order注解的值越小，优先级越高
	public void afterStart(WebServerInitializedEvent event) {
		Environment environment = event.getApplicationContext().getEnvironment();
		// 获取spring.application.name的属性，并转换为大写
		String appName = environment.getProperty("spring.application.name").toUpperCase();
		// 获取当前web服务器的端口号
		int localPort = event.getWebServer().getPort();
		// 获取当前活动的环境变量，并转换为逗号分隔的字符串
		String profile = StringUtils.arrayToCommaDelimitedString(environment.getActiveProfiles());
		// 输出日志
		log.info("---[{}]---启动完成，当前端口为:[{}]，环境变量:[{}]---", appName, localPort, profile);
	}
}
