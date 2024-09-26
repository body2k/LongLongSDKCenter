//package com.longlong.common.secure.interceptor;
//
//import com.longlong.common.api.R;
//import com.longlong.common.api.ResultCode;
//import com.longlong.common.constant.CloudConstant;
//import com.longlong.common.jackson.JsonUtil;
//import com.longlong.common.secure.annotation.APIPermissions;
//import com.longlong.common.secure.props.CloudClientProperties;
//import com.longlong.common.secure.props.CloudSecureProperties;
//import com.longlong.common.secure.props.ServerName;
//import com.longlong.common.secure.utils.SecureUtil;
//import com.longlong.common.utils.RedisUtil;
//import com.longlong.common.utils.SpringUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.web.method.HandlerMethod;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.lang.reflect.Method;
//import java.util.Objects;
//
//@Component
//@EnableConfigurationProperties({ServerName.class})
//@Configuration
//@Slf4j
//public class APIInterceptor extends HandlerInterceptorAdapter {
//    String serverName;
//    @Resource
//    private RedisUtil redisUtil;
//    public APIInterceptor() {
//    }
//
//    public APIInterceptor(String serverName) {
//        this.serverName = serverName;
//    }
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        HandlerMethod handlerMethod = (HandlerMethod) handler;
//        Method method = handlerMethod.getMethod();
//        APIPermissions access = method.getAnnotation(APIPermissions.class);
//        //拦截权限是开启的
//        if (access != null) {
//            if (access.value()) {
//                //获取服务器名称和url
//                System.out.println(serverName  + request.getRequestURI());
//                redisUtil.get("ha:role/users/user/page");
//                if (!request.getRequestURI().equals("/user/page")){
//                    R result = R.fail(ResultCode.NO_AUTHORIZED);
//                    response.setHeader(CloudConstant.CONTENT_TYPE_NAME, MediaType.APPLICATION_JSON_UTF8_VALUE);
//                    response.setCharacterEncoding(CloudConstant.UTF_8);
//                    response.setStatus(HttpServletResponse.SC_OK);
//                    try {
//                        response.getWriter().write(Objects.requireNonNull(JsonUtil.toJson(result)));
//                    } catch (IOException ex) {
//                        log.error(ex.getMessage());
//                    }
//                    return  false;
//                }
//                System.out.println(SecureUtil.getUser().getRoleId());
//            }
//        }
//        System.out.println("----------------------------------------------");
//        return true;
//    }
//}
//