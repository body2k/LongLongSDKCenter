package com.longlong.common.utils;

import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.beans.BeanUtils.instantiateClass;

/**
 * @author 29027 待测试
 */
public class EntityUtil {

    /**
     * 重写转Map
     */
    @SneakyThrows
    public static <T> Map<String, Object> toMap(T clazz) {
        Map<String, Object> map = new HashMap<>(10);
        Class<?> superclass = clazz.getClass();
        /**
         * 这包括公共、受保护、默认（包）访问和私有字段，但不包括继承的字段。
         * */
        Field[] declaredFields1 = superclass.getDeclaredFields();
        do {
            for (Field field : declaredFields1) {
                field.setAccessible(true);
                /**
                 * field.getName()  返回此 Field 对象所表示的字段的名称
                 * 返回指定对象的value值 我指定的user
                 * */
                map.put(field.getName(), field.get(clazz));
            }
            /**
             * 获取继承类
             * */
            superclass = superclass.getSuperclass();
            /**
             * 这包括公共、受保护、默认（包）访问和私有字段，但不包括继承的字段。
             * */
            declaredFields1 = superclass.getDeclaredFields();

            //判断这个是因为所有类都继承了Object的类 Object是所有类的父类
        } while (!"java.lang.Object".equals(superclass.getName()));
        return map;
    }

    public static <T> List<Map<String, Object>> toMap(List<T> clazz) {
        return clazz.stream().map(EntityUtil::toMap).collect(Collectors.toList());
    }

    public static <T> T copy(Object source, Class<T> clazz) {
        T to = newInstance(clazz);

        return copy(source,to);
    }

    /**
     * 实例化对象
     *
     * @param clazz 类
     * @param <T>   泛型标记
     * @return 对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<?> clazz) {
        return (T) instantiateClass(clazz);
    }

    /**
     * 实体类拷贝 严格所有类型必须一直
     * EntityUtil.copy(user,new NsPrice())
     *
     * @param source 被复制的实体类
     * @param clazz  复制的实体类
     * @return 结果 clazz
     */
    @SneakyThrows
    public static <T, V> V copy(T source, V clazz) {
        Field[] fields = source.getClass().getDeclaredFields();
        Field[] fields1 = clazz.getClass().getDeclaredFields();
        Class<?> superclass = source.getClass();
        //先转成key 属性名+字段类型   value 值
        Map<String, Object> map = new HashMap<>();
        do {
            for (Field field : fields) {
                field.setAccessible(true);
                /**
                 * field.getName()  返回此 Field 对象所表示的字段的名称
                 * 返回指定对象的value值 我指定的user
                 * */
                map.put(field.getName() + field.getGenericType(), field.get(source));
            }
            /**
             * 获取继承类
             * */
            superclass = superclass.getSuperclass();
            /**
             * 这包括公共、受保护、默认（包）访问和私有字段，但不包括继承的字段。
             * */
            fields = superclass.getDeclaredFields();
            //判断这个是因为所有类都继承了Object的类 Object是所有类的父类
        } while (!"java.lang.Object".equals(superclass.getName()));
        System.out.println(map);
        Class<?> superclassClazz = clazz.getClass();
        do {
            for (Field field : fields1) {
                field.setAccessible(true);
                if (map.get(field.getName() + field.getGenericType()) != null) {
                    /**
                     * 进行赋值，从map里获取数据并进行赋值  属性名+类型
                     * */
                    field.set(clazz, map.get(field.getName() + field.getGenericType()));
                }
            }
            /**
             * 获取继承类
             * */
            superclassClazz = superclassClazz.getSuperclass();
            /**
             * 这包括公共、受保护、默认（包）访问和私有字段，但不包括继承的字段。
             * */
            fields1 = superclassClazz.getDeclaredFields();
            //判断这个是因为所有类都继承了Object的类 Object是所有类的父类
        } while (!"java.lang.Object".equals(superclassClazz.getName()));
        return clazz;
    }

    /**
     * 集合实体类拷贝 严格所有类型必须一直
     * EntityUtil.copy(user,new NsPrice())
     *
     * @param source 被复制的实体类
     * @param clazz  复制的实体类
     * @return 结果 clazz
     */
    public static <T> List<T> copy(List<Object> source, T clazz) {
        List<T> list = new LinkedList<>();
        for (int i = 0; source.size() > i; i++) {
            T t = EntityUtil.copy(list.get(i), clazz);
            list.add(t);
        }
        return list;
    }

    /**
     * map转实体类
     */
    @SneakyThrows
    public static <T> T toEntityClass(Map<String, Object> map, T clazz) {
        Field[] declaredFields = clazz.getClass().getDeclaredFields();
        Class<?> superclassClazz = clazz.getClass();
        do {
            for (Field field : declaredFields) {
                field.setAccessible(true);
                if (map.get(field.getName()) != null) {
                    System.out.println("赋值");
                    field.set(clazz, map.get(field.getName()));
                }
            }
            /**
             * 获取继承类
             * */
            superclassClazz = superclassClazz.getSuperclass();
            /**
             * 这包括公共、受保护、默认（包）访问和私有字段，但不包括继承的字段。
             * */
            declaredFields = superclassClazz.getDeclaredFields();
            //判断这个是因为所有类都继承了Object的类 Object是所有类的父类
        } while (!"java.lang.Object".equals(superclassClazz.getName()));
        return clazz;
    }

    /**
     * map转实体类
     */
    public static <T> List<T> toEntityClass(List<Map<String, Object>> map, T clazz) {
        return map.stream().map(item -> EntityUtil.toEntityClass(item, clazz)).collect(Collectors.toList());
    }

    /**
     * @param clazz 要处理的数据
     *              实体类转List
     */
    public static <T> List<String> toEntityList(T clazz) throws IllegalAccessException {
        List<String> stringList = new ArrayList<>();
        Class<?> superclass = clazz.getClass();
        /**
         * 这包括公共、受保护、默认（包）访问和私有字段，但不包括继承的字段。
         * */
        Field[] declaredFields1 = superclass.getDeclaredFields();
        do {
            for (Field field : declaredFields1) {
                field.setAccessible(true);
                /**
                 * field.getName()  返回此 Field 对象所表示的字段的名称
                 * 返回指定对象的value值 我指定的user
                 * */
                stringList.add(field.get(clazz).toString());
            }
            /**
             * 获取继承类
             * */
            superclass = superclass.getSuperclass();
            /**
             * 这包括公共、受保护、默认（包）访问和私有字段，但不包括继承的字段。
             * */
            declaredFields1 = superclass.getDeclaredFields();

            //判断这个是因为所有类都继承了Object的类 Object是所有类的父类
        } while (!"java.lang.Object".equals(superclass.getName()));

        return stringList;
    }


    /**
     * 对统计表格使用（绘制echarts） 不支持继承类
     * User user = new User();
     * user.setId(1);
     * user.setAccount("2");
     * User userq = new User();
     * userq.setId(1);
     * userq.setAccount("2");
     * List<User> userList = new ArrayList<>();
     * userList.add(user);
     * userList.add(userq);
     * List<String> stringList = new ArrayList<>();
     * stringList.add("id");
     * stringList.add("account");
     * Map<String, Object> map = EntityUtil.drawEcharts(userList, stringList, stringList);
     * 返回结果 {id=[1, 1], account=[2, 2]}
     *
     * @param data        数据
     * @param listContent 那些字段要被绘制
     * @param listName    要绘制的key名称
     */
    @SneakyThrows
    public static <T> Map<String, List<Object>> drawEcharts(List<T> data, List<String> listContent, List<String> listName) {
        Map<String, List<Object>> map = new HashMap<>();
        for (int i = 0; i < listContent.size(); i++) {
            List<Object> names = new ArrayList<>();
            //循环结构
            for (T item : data) {
                Field declaredField = item.getClass().getDeclaredField(listContent.get(i));
                declaredField.setAccessible(true);
                names.add(declaredField.get(item) != null ? declaredField.get(item).toString() : 0);
            }
            map.put(listName.get(i), names);
        }
        return map;
    }


    /**
     * 对统计表格使用（绘制echarts）
     * User user = new User();
     * user.setId(1);
     * user.setAccount("2");
     * User userq = new User();
     * userq.setId(1);
     * userq.setAccount("2");
     * List<User> userList = new ArrayList<>();
     * userList.add(user);
     * userList.add(userq);
     * List<String> stringList = new ArrayList<>();
     * stringList.add("id");
     * stringList.add("account");
     * Map<String, Object> map = EntityUtil.drawEcharts(userList, stringList, stringList);
     * 返回结果 {id=[1, 1], account=[2, 2]}
     *
     * @param data        数据
     * @param listContent 那些字段要被绘制
     */
    @SneakyThrows
    public static <T> Map<String, List<Object>> drawEcharts(List<T> data, List<String> listContent) {
        Map<String, List<Object>> map = new HashMap<>();
        for (int i = 0; i < listContent.size(); i++) {
            List<Object> names = new ArrayList<>();
            //循环结构
            for (T item : data) {
                Field declaredField = item.getClass().getDeclaredField(listContent.get(i));
                declaredField.setAccessible(true);
                names.add(declaredField.get(item) != null ? declaredField.get(item).toString() : 0);
            }
            map.put(listContent.get(i), names);
        }
        return map;
    }


}

