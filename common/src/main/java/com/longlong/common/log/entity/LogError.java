package com.longlong.common.log.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.longlong.common.mybatis.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 错误日志表实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LogError extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 1主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 2租户ID
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
     *5 系统环境
     */
    private String env;

    /**
     *6 标题
     * */
    private String title;

    /**
     * 7操作方式
     */
    private String method;
    /**
     * 8请求URI
     */
    private String requestUri;

    /**
     * 9堆栈
     */
    private String stackTrace;
    /**
     * 10异常名
     */
    private String exceptionName;
    /**
     * 11异常信息
     */
    private String message;
    /**
     * 12错误行数
     */
    private Integer lineNumber;
    /**
     * 13操作IP地址
     */
    private String remoteIp;
    /**
     * 14方法类
     */
    private String methodClass;
    /**
     * 15方法名
     */
    private String methodName;
    /**
     * 16操作提交的数据
     */
    private String params;
    /**
     *  17执行时间
     */
    private Long time;


    @Override
    public String toString() {
        return "LogError{" +
                "id=" + id +
                ", tenantId=" + tenantId +
                ", serverHost=" + serverHost +
                ", serverIp=" + serverIp +
                ", env=" + env +
                ", method=" + method +
                ", requestUri=" + requestUri +
                ", stackTrace=" + stackTrace +
                ", exceptionName=" + exceptionName +
                ", message=" + message +
                ", lineNumber=" + lineNumber +
                ", remoteIp=" + remoteIp +
                ", methodClass=" + methodClass +
                ", methodName=" + methodName +
                ", params=" + params +
                ", time=" + time +
                "}";
    }
}
