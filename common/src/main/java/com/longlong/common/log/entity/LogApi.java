package com.longlong.common.log.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.longlong.common.mybatis.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 接口日志表实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LogApi extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 1主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 22租户ID
     */
    private String tenantId;

    /**
     * 3服务器名
     */
    private String serverHost;
    /**
     * 4服务器IP地址
     */
    private String serverIp;
    /**
     *5 服务器环境
     */
    private String env;
    /**
     * 6日志标题
     */
    private String title;
    /**
     *7 操作方式
     */
    private String method;
    /**
     *8 请求URI
     */
    private String requestUri;

    /**
     * 9操作IP地址
     */
    private String remoteIp;
    /**
     *10 方法类
     */
    private String methodClass;
    /**
     * 11方法名
     */
    private String methodName;
    /**
     *12 操作提交的数据
     */
    private String params;
    /**
     * 13执行时间
     */
    private Long time;


    @Override
    public String toString() {
        return "LogApi{" +
                "id=" + id +
                ", tenantId=" + tenantId +
                ", serverHost=" + serverHost +
                ", serverIp=" + serverIp +
                ", env=" + env +
                ", title=" + title +
                ", method=" + method +
                ", requestUri=" + requestUri +
                ", remoteIp=" + remoteIp +
                ", methodClass=" + methodClass +
                ", methodName=" + methodName +
                ", params=" + params +
                ", time=" + time +
                "}";
    }
}
