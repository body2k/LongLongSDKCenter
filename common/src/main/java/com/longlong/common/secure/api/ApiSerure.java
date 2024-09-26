package com.longlong.common.secure.api;

import com.longlong.common.api.R;
import com.longlong.common.log.Log;
import com.longlong.common.secure.CloudUser;
import com.longlong.common.secure.annotation.APIPermissions;
import com.longlong.common.secure.utils.SecureUtil;
import com.longlong.common.utils.RedisUtil;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
@Data
public class ApiSerure {

    @Resource
    private RedisUtil redisUtil;
    @Value("${spring.application.name}")
    private String appName;

    @Pointcut("@annotation(com.longlong.common.secure.annotation.APIPermissions)")
    private void doApi() {

    }

    @Around("doApi()")
    private Object doAround(ProceedingJoinPoint point) throws Throwable {
        System.out.println("-==============================");
        //获取目标对象类型(切入点方法所在类的类型)
        Class<?> targetClass = point.getTarget().getClass();
        //获取方法签名(包含方法信息)
        MethodSignature signature = (MethodSignature) point.getSignature();
        //获取方法对象
        Method method = targetClass.getDeclaredMethod(
                signature.getName(), signature.getParameterTypes());
        APIPermissions annotation = method.getAnnotation(APIPermissions.class);
        if (annotation != null && annotation.value()) {
            RequestMapping annotation1 = method.getAnnotation(RequestMapping.class);
            System.out.println("-------------------------");
            ServletRequestAttributes requestAttributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            String requestURI = requestAttributes.getRequest().getRequestURI();
            System.out.println(appName + requestURI);
            CloudUser user = SecureUtil.getUser();
            if (user != null) {
                List<String> list = Arrays.asList(user.getRoleId().split(","));
                List<String> collect = list.stream().map(item -> "roleId" + item + ":" + appName + requestURI).collect(Collectors.toList());
                long o = redisUtil.countExistingKeys(collect);
                if (o > 0) {
                    System.out.println("存在");
                } else {
                    //没有权限
                    return R.fail("没有权限");
                }
            } else {
                System.out.println("用户不存在");
            }
        }
        return point.proceed();
    }
}
