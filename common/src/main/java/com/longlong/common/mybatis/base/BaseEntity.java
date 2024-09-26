
package com.longlong.common.mybatis.base;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.longlong.common.serializer.NullIntegerSerializer;

import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

import com.longlong.common.utils.DateUtil;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础实体类
 */
@Data
public class BaseEntity implements Serializable {

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @JsonSerialize(nullsUsing = NullIntegerSerializer.class)
    private Integer createUser;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    @JsonSerialize(nullsUsing = NullIntegerSerializer.class)
    private Integer updateUser;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 状态0正常
     */
    @ApiModelProperty(value = "业务状态")
    @JsonSerialize(nullsUsing = NullIntegerSerializer.class)
    private Integer status;

    /**
     * 状态[0:未删除,1:删除]
     */

    @ApiModelProperty(value = "是否已删除")
    @JsonSerialize(nullsUsing = NullIntegerSerializer.class)
    @TableLogic
    private Integer isDeleted;

    @Version
    private Integer version;

    @ApiModelProperty(value = "租户")
    private String tenantId;


}
