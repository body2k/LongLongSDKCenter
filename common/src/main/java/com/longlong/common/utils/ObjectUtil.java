
package com.longlong.common.utils;

import org.springframework.lang.Nullable;

/**
 * 对象工具类
 *
 * 
 */
public class ObjectUtil extends org.springframework.util.ObjectUtils {

	/**
	 * 判断元素不为空
	 * @param obj object
	 * @return boolean
	 */
	public static boolean isNotEmpty(@Nullable Object obj) {
		return !ObjectUtil.isEmpty(obj);
	}

	/**
	 * 判断是否不为空
	 * @param obj 判断的参数
	 * ObjectUtil.isNotBlank(null) false
	 * ObjectUtil.isNotBlank("") false
	 * ObjectUtil.isNotBlank(" ") false
	 * ObjectUtil.isNotBlank(1) true
	 * ObjectUtil.isNotBlank("1") true
	 * @return boolean
	 * */
	public static boolean isNotBlank(@Nullable Object obj) {
		if (obj instanceof String){
			return StringUtil.isNotBlank(obj.toString());
		}
		return !ObjectUtil.isEmpty(obj);
	}
	/**
	 *  判断是空
	 * @param obj 判断的参数
	 * ObjectUtil.isNotBlank(null) true
	 * ObjectUtil.isNotBlank("") true
	 * ObjectUtil.isNotBlank(" ") true
	 * ObjectUtil.isNotBlank(1) false
	 * ObjectUtil.isNotBlank("1") false
	 * @return boolean
	 * */
	public static boolean isBlank(@Nullable Object obj) {
		if (obj instanceof String){
			return StringUtil.isBlank(obj.toString());
		}
		return ObjectUtil.isEmpty(obj);
	}


}
