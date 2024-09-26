package com.longlong.common.log;
import org.apache.commons.dbcp2.BasicDataSource;

import com.longlong.common.api.R;
import com.longlong.common.jackson.JsonUtil;
import com.longlong.common.log.entity.LogApi;
import com.longlong.common.log.entity.LogError;
import com.longlong.common.logger.RequestLogutil;

import com.longlong.common.secure.CloudUser;
import com.longlong.common.secure.utils.SecureUtil;
import com.longlong.common.utils.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.*;

import org.springframework.beans.factory.annotation.Value;

import javax.sql.DataSource;


@Slf4j
@Data

@Aspect
@Component
@ConfigurationProperties("log.mysql")
public class LogProperties {

    @Value("${spring.application.name}")
    private String appName;
    @Value("${server.port}")
    private String prop;
    @Value("${spring.profiles.active}")
    private String env;

    //是否开启插入日志
    private Boolean isOpenLog = true;
    //日志模块服务名
    private Boolean serverName;
    //数据库url
    @Value("${spring.datasource.url}")
    private String url;
    //数据库账号
    @Value("${spring.datasource.username}")
    private String username;
    //数据库密码
    @Value("${spring.datasource.password}")
    private String password;
    //数据库驱动
    private String driverName = "com.mysql.cj.jdbc.Driver";
    //是否开启多表记录日志 默认开启
    private Boolean isMultipleTables = false;
    //生成规则默认yyMMdd 只能是时间规则如yyyy-MM-dd
    private String regulations = "yyMMdd";



    DataSource dataSource =null;
            //    static {
//        dataSource.setUrl(url);
//        dataSource.setUsername(username);
//        dataSource.setPassword(password);
//        dataSource.setDriverClassName(driverName);
//    }
//
    ;
    JdbcTemplate jdbcTemplate = null;


    public Boolean getOpenLog() {
        return isOpenLog;
    }

    public void setOpenLog(Boolean openLog) {
        if (openLog == null) {
            isOpenLog = false;
        } else {
            isOpenLog = true;
        }

    }

    //创建数据库链接池子
    @SneakyThrows
    @Async
    @Order
    @EventListener(WebServerInitializedEvent.class)
    public void createDatabaseLog() {
        if (isOpenLog) {
            BasicDataSource basicDataSource = new BasicDataSource ();
            log.info("数据库日志连接池以打开");
            basicDataSource.setUrl(url);
            basicDataSource.setUsername(username);
            basicDataSource.setPassword(password);
            basicDataSource.setDriverClassName(driverName);
            dataSource=basicDataSource;
//            Connection connection = DataSourceUtils.getConnection(driverManagerDataSource);
//            DataSourceUtils.get
          jdbcTemplate= new JdbcTemplate(dataSource);

        }
    }


    @Pointcut("@annotation(com.longlong.common.log.Log)")
    private void doLog() {

    }

    @Around("doLog()")
    private Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {

        //数据原打开
        if (isOpenLog) {
            //开始时长
            long startTime = System.currentTimeMillis();
            ;
            //结束时长
            long endTime = 0L;
            Object result = null;
            Exception error = new Exception();
            //0正常 1异常
            int errorStatus = 0;
            try {
                result = joinPoint.proceed();
                //结束时长
                endTime = System.currentTimeMillis();
            } catch (Exception exception) {
                //结束时长
                endTime = System.currentTimeMillis();
                error = exception;
                errorStatus = 1;
            } finally {
                long ingTime = endTime - startTime;
                //正常
                if (errorStatus == 0) {
                    saveLogAPI(joinPoint, ingTime);
                    //异常
                } else {
                    saveLogError(joinPoint, ingTime, error);
                }
            }
            return errorStatus == 0 ? result : R.status(false);
            //查询数据库
//            List<String> showTables = jdbcTemplate.queryForList("show tables" , String.class);

        } else {
            return joinPoint.proceed();
        }


    }


    public Boolean saveLogAPI(ProceedingJoinPoint point, long time) throws NoSuchMethodException {
        LogCreateConstant logCreateConstant = new LogCreateConstant();
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        //获取目标对象类型(切入点方法所在类的类型)
        Class<?> targetClass = point.getTarget().getClass();
        //获取方法签名(包含方法信息)
        MethodSignature signature = (MethodSignature) point.getSignature();
        //获取方法对象
        Method method = targetClass.getDeclaredMethod(
                signature.getName(), signature.getParameterTypes());
        // 获取目标方法上的注解
        Log requiredLog = method.getAnnotation(Log.class);
        // 日志标题
        String title = requiredLog.value();
        //方法名
        String methodName = targetClass.getName() + "." + method.getName();

        LogApi logApi = new LogApi();
        CloudUser user = SecureUtil.getUser();
        if (user != null) {
            logApi.setCreateUser(user.getUserId());
            logApi.setUpdateUser(user.getUserId());
            logApi.setTenantId(user.getTenantId());
        }
        Date now = DateUtil.now();
        logApi.setCreateTime(now);
        logApi.setUpdateTime(now);
        //执行时间
        logApi.setTime(time);
        //操作提交的数据
        logApi.setParams(JsonUtil.toJson(RequestLogutil.paramsToString(point)));
        //方法名
        logApi.setMethodName(methodName);
        //方法类
        logApi.setMethodClass(targetClass.getName());
        //操作IP地址
        logApi.setRemoteIp(WebUtil.getIP(request));
        //请求URI
        logApi.setRequestUri(requestAttributes.getRequest().getRequestURI());
        //操作方式
        logApi.setMethod(requestAttributes.getRequest().getMethod());
        //日志标题
        logApi.setTitle(title);
        String localIp;
        try {
            localIp = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            localIp = "0.0.0.0";
        }
        logApi.setEnv(env);
        logApi.setServerIp(localIp + prop);
        logApi.setServerHost(appName);


        logCreateConstant.setIsMultipleTables(isMultipleTables);
        logCreateConstant.setRegulations(regulations);


        try {
            int update = insertSqlLogApi(logCreateConstant, logApi);
        } catch (Exception e) {
            //如果更新行数未0 则查询数据库是否存在不存在则创建表
            if (jdbcTemplate.queryForObject(logCreateConstant.getSelectLogApiTableNameTable(), Integer.class, DBUtil.dbUrlToDBName(url)) == 0) {
                log.info("----------" + logCreateConstant.getLogApiTableName() + "表不存在,生成表" + logCreateConstant.getLogApiTableName() + "------------");

                jdbcTemplate.execute(logCreateConstant.getCreateLogApiSql());
                insertSqlLogApi(logCreateConstant, logApi);
            }

            log.info("日志插入失败");

        }
        return true;
    }

    public Boolean saveLogError(ProceedingJoinPoint point, long time, Exception error) throws NoSuchMethodException {
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        jakarta.servlet.http.HttpServletRequest request = requestAttributes.getRequest();
        //获取目标对象类型(切入点方法所在类的类型)
        Class<?> targetClass = point.getTarget().getClass();
        //获取方法签名(包含方法信息)
        MethodSignature signature = (MethodSignature) point.getSignature();
        //获取方法对象
        Method method = targetClass.getDeclaredMethod(
                signature.getName(), signature.getParameterTypes());
        // 获取目标方法上的注解
        Log requiredLog = method.getAnnotation(Log.class);
        // 日志标题
        String title = requiredLog.value();
        //方法名
        String methodName = targetClass.getName() + "." + method.getName();
        LogError logError = new LogError();
        CloudUser user = SecureUtil.getUser();
        if (user != null) {
            logError.setCreateUser(user.getUserId());
            logError.setUpdateUser(user.getUserId());
            logError.setTenantId(user.getTenantId());
        }
        Date now = DateUtil.now();
        logError.setCreateTime(now);
        logError.setUpdateTime(now);

        //执行时间
        logError.setTime(time);
        //操作提交的数据
        logError.setParams(JsonUtil.toJson(RequestLogutil.paramsToString(point)));
        //方法名
        logError.setMethodName(methodName);
        //方法类
        logError.setMethodClass(targetClass.getName());
        //操作IP地址
        logError.setRemoteIp(WebUtil.getIP(request));
        //请求URI
        logError.setRequestUri(requestAttributes.getRequest().getRequestURI());
        //异常行数
        logError.setLineNumber(Exceptions.getExceptionLineNumber(error));
        //异常信息
        logError.setMessage(error.getMessage());
        //异常名
        logError.setExceptionName(error.getClass().getName());
        //堆栈
        logError.setStackTrace(Exceptions.getExceptionStackTrace(error));
        //操作方式
        logError.setMethod(requestAttributes.getRequest().getMethod());
        //日志标题
        logError.setTitle(title);
        String localIp;
        try {
            localIp = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            localIp = "0.0.0.0";
        }
        logError.setEnv(env);
        logError.setServerIp(localIp + prop);
        logError.setServerHost(appName);
        LogCreateConstant logCreateConstant = new LogCreateConstant();
        logCreateConstant.setIsMultipleTables(isMultipleTables);
        logCreateConstant.setRegulations(regulations);
        try {
            int update = insetSqlLogError(logCreateConstant,logError);
        } catch (Exception e) {
            //如果更新行数未0 则查询数据库是否存在不存在则创建表
            if (jdbcTemplate.queryForObject(logCreateConstant.getSelectLogErrorTableNameTable(), Integer.class, DBUtil.dbUrlToDBName(url)) == 0) {
                log.info("----------" + logCreateConstant.getLogErrorTableName() + "表不存在,生成表" + logCreateConstant.getLogErrorTableName() + "------------");
                jdbcTemplate.execute(logCreateConstant.getCreateLogErrorSql());
                insetSqlLogError(logCreateConstant,logError);
            }

            log.info("日志插入失败");

        }
        return true;
    }

    public Integer insertSqlLogApi(LogCreateConstant logCreateConstant, LogApi logApi) {

        return jdbcTemplate.update(logCreateConstant.getInsertLogApiSql(),
                logApi.getTenantId(),
                logApi.getServerHost(),
                logApi.getServerIp(),
                logApi.getEnv(),
                logApi.getTitle(),
                logApi.getMethod(),
                logApi.getRequestUri(),
                logApi.getRemoteIp(),
                logApi.getMethodClass(),
                logApi.getMethodName(),
                logApi.getParams(),
                logApi.getTime(),
                logApi.getCreateUser(),
                logApi.getCreateTime(),
                logApi.getUpdateUser(),
                logApi.getUpdateTime(),
                0
        );
    }


    public Integer insetSqlLogError(LogCreateConstant logCreateConstant, LogError logError) {
//        LogCreateConstant logCreateConstant=new LogCreateConstant();
        return jdbcTemplate.update(logCreateConstant.getInsertLogErrorSql(),
                logError.getTenantId(),
                logError.getServerHost(),
                logError.getServerIp(),
                logError.getEnv(),
                logError.getTitle(),
                logError.getMethod(),
                logError.getRequestUri(),
                logError.getStackTrace(),
                logError.getExceptionName(),
                logError.getMessage(),
                logError.getLineNumber(),
                logError.getRemoteIp(),
                logError.getMethodClass(),
                logError.getMethodName(),
                logError.getParams(),
                logError.getTime(),
                logError.getCreateUser(),
                logError.getCreateTime(),
                logError.getUpdateUser(),
                logError.getUpdateTime(),
                0
        );
    }

}
