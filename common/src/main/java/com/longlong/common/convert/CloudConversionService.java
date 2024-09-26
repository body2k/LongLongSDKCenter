package com.longlong.common.convert;

import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.lang.Nullable;
import org.springframework.util.StringValueResolver;

/**
 * 类型 转换 服务，添加了 IEnum 转换
 *
 * 
 */
public class CloudConversionService extends ApplicationConversionService {
	@Nullable
	private static volatile CloudConversionService SHARED_INSTANCE;

	public CloudConversionService() {
		this(null);
	}

	public CloudConversionService(@Nullable StringValueResolver embeddedValueResolver) {
		super(embeddedValueResolver);
		super.addConverter(new EnumToStringConverter());
		super.addConverter(new StringToEnumConverter());
	}

	/**
	 * Return a shared default application {@code ConversionService} instance, lazily
	 * building it once needed.
	 * <p>
	 * Note: This method actually returns an {@link CloudConversionService}
	 * instance. However, the {@code ConversionService} signature has been preserved for
	 * binary compatibility.
	 * @return the shared {@code BladeConversionService} instance (never{@code null})
	 */
	public static GenericConversionService getInstance() {
		CloudConversionService sharedInstance = CloudConversionService.SHARED_INSTANCE;
		if (sharedInstance == null) {
			synchronized (CloudConversionService.class) {
				sharedInstance = CloudConversionService.SHARED_INSTANCE;
				if (sharedInstance == null) {
					sharedInstance = new CloudConversionService();
					CloudConversionService.SHARED_INSTANCE = sharedInstance;
				}
			}
		}
		return sharedInstance;
	}

}
