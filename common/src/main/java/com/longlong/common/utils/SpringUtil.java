
package com.longlong.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;

import javax.sql.DataSource;

/**
 * spring 工具类
 *
 * 
 */
@Slf4j
public class SpringUtil implements ApplicationContextAware {

	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		SpringUtil.context = context;
	}


	/**
	 * 等于ApplicationContext.getBean() 方法
	 * 返回唯一匹配给定对象类型的 Bean 实例
	 * */
	public static <T> T getBean(Class<T> clazz) {
		if (clazz == null) {
			return null;
		}
		return context.getBean(clazz);
	}

	public static <T> T getBean(String beanId) {
		if (beanId == null) {
			return null;
		}
		return (T) context.getBean(beanId);
	}

	public static <T> T getBean(String beanName, Class<T> clazz) {
		if (null == beanName || "".equals(beanName.trim())) {
			return null;
		}
		if (clazz == null) {
			return null;
		}
		return (T) context.getBean(beanName, clazz);
	}

	//获取ApplicationContext
	public static ApplicationContext getContext() {
		//如果context为空，则返回null
		if (context == null) {
			return null;
		}
		//否则返回context
		return context;
	}

	//发布事件
	public static void publishEvent(ApplicationEvent event) {
		//如果context为空，则直接返回
		if (context == null) {
			return;
		}
		//否则尝试发布事件
		try {
			context.publishEvent(event);
		} catch (Exception ex) {
			//如果发布事件失败，则记录错误信息
			log.error(ex.getMessage());
		}
	}
	// 从上下文中获取DataSource类型的bean，并返回
	public static DataSource getDataSource() {
		return context.getBean(DataSource.class);
	}
}
