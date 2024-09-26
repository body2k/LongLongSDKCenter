
package com.longlong.common.secure;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * AuthInfo
 */
@Data
public class AuthInfo {

    /**
     * 令牌
     */
    private String accessToken;
    /*令牌类型*/
    private String tokenType;
    /*刷新令牌"*/
    private String refreshToken;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 角色名
     */
    private String authority;
    /**
     * 角色ids
     * */
    private String roleIds;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 账号名
     */
    private String account;
    /**
     * 过期时间
     */
    private long expiresIn;
    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 用户code
     */
    private String userCode;
    /**
     * 用户是否授权
     */
    private String authentication;
    /**
     * 许可证
     * */
    private String license ;

    /**
     * 租户Id
     **/
    private String tenantId;
    /**
     * 第三方授权ID
     * */
    private String oauthId;
    /**
     * 租户类型
     * */
    private Integer tenantType;

    private Integer userType;
}
