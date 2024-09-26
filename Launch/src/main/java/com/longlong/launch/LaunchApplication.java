package com.longlong.launch;

import com.longlong.constant.AppConstant;
import com.longlong.service.LauncherService;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.*;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 启动类
 */
public class LaunchApplication {


    /**
     * @param appName 服务名称
     * @param source  启动类实体类
     * @param args    man方法
     */
    public static ConfigurableApplicationContext run(String appName, Class source, String... args) {
        SpringApplicationBuilder builder = createSpringApplicationBuilder(appName, source, null, args);
        return builder.run(args);
    }

    /**
     * @param appName 服务名称
     * @param source  启动类实体类
     * @param args    man方法
     * @param port    端口
     */
    public static ConfigurableApplicationContext run(String appName, Integer port, Class source, String... args) {
        SpringApplicationBuilder builder = createSpringApplicationBuilder(appName, source, port, args);
        return builder.run(args);
    }

    /**
     * @param appName 服务名称
     * @param source  启动类实体类
     * @param args    man方法
     * @param port    端口
     */

    public static SpringApplicationBuilder createSpringApplicationBuilder(String appName, Class source, Integer port, String... args) {
        Assert.hasText(appName, "服务名不能为空");
        // 读取环境变量，使用spring boot的规则
        ConfigurableEnvironment environment = new StandardEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();
        propertySources.addFirst(new SimpleCommandLinePropertySource(args));
        propertySources.addLast(new MapPropertySource(StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME, environment.getSystemProperties()));
        propertySources.addLast(new SystemEnvironmentPropertySource(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, environment.getSystemEnvironment()));

        // 获取配置的环境变量
        String[] activeProfiles = environment.getActiveProfiles();
        // 判断环境:dev、test、prod
        List<String> profiles = Arrays.asList(activeProfiles);
        // 预设的环境
        List<String> presetProfiles = new ArrayList<>(Arrays.asList(AppConstant.DEV_CODE, AppConstant.TEST_CODE, AppConstant.PROD_CODE));
        // 交集
        presetProfiles.retainAll(profiles);
        // 当前使用
        List<String> activeProfileList = new ArrayList<>(profiles);
        Function<Object[], String> joinFun = StringUtils::arrayToCommaDelimitedString;
        SpringApplicationBuilder builder = new SpringApplicationBuilder(source);
        String profile;
        if (activeProfileList.isEmpty()) {
            // 默认dev开发
            profile = AppConstant.DEV_CODE;
            activeProfileList.add(profile);
            builder.profiles(profile);
        } else if (activeProfileList.size() == 1) {
            profile = activeProfileList.get(0);
        } else {
            // 同时存在dev、test、prod环境时
            throw new RuntimeException("同时存在环境变量:[" + StringUtils.arrayToCommaDelimitedString(activeProfiles) + "]");
        }
        String startJarPath = LaunchApplication.class.getResource("/").getPath().split("!")[0];
        String activePros = joinFun.apply(activeProfileList.toArray());
        System.out.println(String.format("----启动中，读取到的环境变量:[%s]，jar地址:[%s]----", activePros, startJarPath));
        System.out.println("全世界无产阶级联合起来");
        Properties props = System.getProperties();
        //服务名称
        props.setProperty("spring.application.name", appName);
        props.setProperty("info.version", AppConstant.APPLICATION_VERSION);
        props.setProperty("info.desc", appName);
        props.setProperty("cloud.env", profile);
        props.setProperty("cloud.name", appName);
        props.setProperty("cloud.is-local", String.valueOf(isLocalDev()));
        props.setProperty("cloud.dev-mode", profile.equals(AppConstant.PROD_CODE) ? "false" : "true");
        props.setProperty("cloud.service.version", AppConstant.APPLICATION_VERSION);
        props.setProperty("spring.main.allow-bean-definition-overriding", "true");
        //端口
        if (port != null) {
            props.setProperty("server.port", String.valueOf(port));

        }


        // 加载自定义组件
        List<LauncherService> launcherList = new ArrayList<>();
        ServiceLoader.load(LauncherService.class).forEach(launcherList::add);
        launcherList.stream().sorted(Comparator.comparing(LauncherService::getOrder)).collect(Collectors.toList())
                .forEach(launcherService -> launcherService.launcher(builder, appName, profile));
        return builder;
    }

    /**
     * 判断当前系统
     * LINUX 返回false
     */
    public static boolean isLocalDev() {
        //当前系统
        String osName = System.getProperty("os.name");
        return StringUtils.hasText(osName) && !(AppConstant.OS_NAME_LINUX.equals(osName.toUpperCase()));
    }


    /**
     * 判断当前端口
     */
    public static boolean isPort(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            // 如果能够绑定到端口，则端口未被占用
            return false;
        } catch (Exception e) {
            // 如果不能绑定到端口，则端口已被占用
            return true;
        }
    }
}
