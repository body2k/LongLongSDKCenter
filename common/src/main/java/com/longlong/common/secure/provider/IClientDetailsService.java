
package com.longlong.common.secure.provider;

/**
 * 多终端注册接口
 *
 * 
 */
public interface IClientDetailsService {

	/**
	 * 根据clientId获取Client详情
	 *
	 * @param clientId 客户端id
	 * @return IClientDetails
	 */
	IClientDetails loadClientByClientId(String clientId);

	/**
	 * 根据clientId获取Client详情
	 *
	 * @param clientId 客户端id
	 * @param tenantId 租户ID
	 * @return IClientDetails
	 */
	IClientDetails loadClientByClientId(String clientId,String tenantId);

}
