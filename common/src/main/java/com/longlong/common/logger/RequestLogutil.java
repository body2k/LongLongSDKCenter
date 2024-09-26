package com.longlong.common.logger;

import com.longlong.common.jackson.JsonUtil;
import com.longlong.common.utils.BeanUtil;
import com.longlong.common.utils.ClassUtil;
import com.longlong.common.utils.StringUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.InputStreamSource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestLogutil {
    /**
     * 获取参数
     * */
    public static Map<String, Object> params( ProceedingJoinPoint point){
        Map<String, Object> paraMap=new HashMap<>(16);
        // 获取方法签名
        MethodSignature ms = (MethodSignature) point.getSignature();
        Method method = ms.getMethod();
        Object[] args = point.getArgs();

        for (int i = 0; i < args.length; i++) {
            // 读取方法参数
            MethodParameter methodParam = ClassUtil.getMethodParameter(method, i);
            // PathVariable 参数跳过
            PathVariable pathVariable = methodParam.getParameterAnnotation(PathVariable.class);
            if (pathVariable != null) {
                System.out.println(pathVariable);
                continue;
            }
            RequestBody requestBody = methodParam.getParameterAnnotation(RequestBody.class);
            Object value = args[i];
            // 如果是body的json则是对象
            if (requestBody != null && value != null) {
                System.out.println(value);
                paraMap.putAll(BeanUtil.toMap(value));
                continue;
            }
            // 处理 List
            if (value instanceof List) {
                value = ((List) value).get(0);
            }
            // 处理 参数
            if (value instanceof HttpServletRequest) {
                paraMap.putAll(((HttpServletRequest) value).getParameterMap());
            } else if (value instanceof WebRequest) {
                paraMap.putAll(((WebRequest) value).getParameterMap());
            } else if (value instanceof MultipartFile) {
                MultipartFile multipartFile = (MultipartFile) value;
                String name = multipartFile.getName();
                String fileName = multipartFile.getOriginalFilename();
                paraMap.put(name, fileName);
            } else if (value instanceof HttpServletResponse) {
            } else if (value instanceof InputStream) {
            } else if (value instanceof InputStreamSource) {
            } else {
                // 参数名
                RequestParam requestParam = methodParam.getParameterAnnotation(RequestParam.class);
                String paraName;
                if (requestParam != null && StringUtil.isNotBlank(requestParam.value())) {
                    paraName = requestParam.value();
                } else {
                    paraName = methodParam.getParameterName();
                }
                paraMap.put(paraName, value);
            }
        }
        return paraMap;
    }
    /**
     * 获取参数
     * */
    public static String paramsToString( ProceedingJoinPoint point){
        Map<String, Object> paraMap = params(point);
        return JsonUtil.toJson(paraMap.isEmpty()?"":JsonUtil.toJson(paraMap));
    }

}
