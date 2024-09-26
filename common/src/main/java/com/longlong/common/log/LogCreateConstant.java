package com.longlong.common.log;

import com.longlong.common.utils.DBUtil;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class LogCreateConstant {

    //是否开启多表记录日志 默认开启
    @Setter
    @Getter
    private Boolean isMultipleTables = true;
    //生成规则默认yyMMdd 只能是时间规则如yyyy-MM-dd
    @Setter
    @Getter
    private String regulations = "yyMMdd";


    public String getTableTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getRegulations());
        return simpleDateFormat.format(new Date());
    }


    public String getLogApiTableName() {

        return getIsMultipleTables() ? "ha_log_api" + getTableTime() : "ha_log_api";
    }

    public String getLogErrorTableName() {
        return getIsMultipleTables() ? "ha_log_error" + getTableTime() : "ha_log_error";
    }

    public String getSelectLogApiTableNameTable() {
        return "select COUNT(1) as quantity from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA=? and TABLE_NAME='" + getLogApiTableName() + "' ;";
    }


    public String getSelectLogErrorTableNameTable() {
        return "select COUNT(1) as quantity from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA=? and TABLE_NAME='" + getLogErrorTableName() + "' ;";
    }

    // 创建 ha_log_api
    public String getCreateLogApiSql() {
        return "CREATE TABLE `" + getLogApiTableName() + "`  (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',\n" +
                "    `tenant_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户ID',\n" +
                "  `server_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务器名',\n" +
                "  `server_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务器IP地址',\n" +
                "  `env` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务器环境',\n" +
                "  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '日志标题',\n" +
                "  `method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作方式',\n" +
                "  `request_uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求URI',\n" +
                "  `remote_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作IP地址',\n" +
                "  `method_class` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '方法类',\n" +
                "  `method_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '方法名',\n" +
                "  `params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '操作提交的数据',\n" +
                "  `time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行时间',\n" +
                "  `create_user` int(64) NULL DEFAULT NULL COMMENT '创建者',\n" +
                "  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',\n" +
                "  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新用户',\n" +
                "  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',\n" +
                "  `is_deleted` int(11) NULL DEFAULT NULL COMMENT '是否删除',\n" +
                "`status` int(11) NULL DEFAULT 0 COMMENT '状态'," +
                "  `version` int(11) NULL DEFAULT 0,"+

                "  PRIMARY KEY (`id`) USING BTREE\n" +
                ") ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '接口日志表' ROW_FORMAT = Dynamic;";


    }


    public String getInsertLogApiSql() {

        return "INSERT INTO `" + getLogApiTableName() + "` (`id`, `tenant_id`, `server_host`, `server_ip`, `env`, `title`, `method`, `request_uri`, `remote_ip`, `method_class`, `method_name`, `params`, `time`, `create_user`, `create_time`, `update_user`, `update_time`, `is_deleted`) " +
                "VALUES (0, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    }


    public String getInsertLogErrorSql() {
        return "INSERT INTO `" + getLogErrorTableName() + "`.`ha_log_error` (`id`, `tenant_id`, `server_host`, `server_ip`, `env`, `title`, `method`, `request_uri`, `stack_trace`, `exception_name`, `message`, `line_number`, `remote_ip`, `method_class`, `method_name`, `params`, `time`, `create_user`, `create_time`, `update_user`, `update_time`, `is_deleted`) " +
                "VALUES (0, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);\n";
    }

    // 创建 LogError
    public String getCreateLogErrorSql() {
        return "CREATE TABLE `" + getLogErrorTableName() + "`  (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',\n" +
                "  `tenant_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户ID',\n" +
                "  `server_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务器名',\n" +
                "  `server_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务器IP地址',\n" +
                "  `env` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '系统环境',\n" +
                "  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',\n" +
                "  `method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作方式',\n" +
                "  `request_uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求URI',\n" +
                "  `stack_trace` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '堆栈',\n" +
                "  `exception_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '异常名',\n" +
                "  `message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '异常信息',\n" +
                "  `line_number` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '错误行数',\n" +
                "  `remote_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作IP地址',\n" +
                "  `method_class` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '方法类',\n" +
                "  `method_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '方法名',\n" +
                "  `params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '操作提交的数据',\n" +
                "  `time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行时间',\n" +
                "  `create_user` int(64) NULL DEFAULT NULL COMMENT '创建者',\n" +
                "  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',\n" +
                "  `update_user` int(11) NULL DEFAULT NULL COMMENT '更新用户',\n" +
                "  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',\n" +
                "  `is_deleted` int(11) NULL DEFAULT NULL COMMENT '是否删除',\n" +
                "`status` int(11) NULL DEFAULT 0 COMMENT '状态'," +
                "  `version` int(11) NULL DEFAULT 0,"+
                "  PRIMARY KEY (`id`) USING BTREE\n" +
                ") ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '错误日志表' ROW_FORMAT = Dynamic;";

    }
}



