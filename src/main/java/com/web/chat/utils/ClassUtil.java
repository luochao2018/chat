package com.web.chat.utils;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 泛型,格式转换
 *
 * @param <T>
 */
public class ClassUtil<T> {
    private static Logger logger = LoggerFactory.getLogger(Object.class);

    /**
     * 根据路径获取类
     *
     * @param path 路径
     * @param <T>
     * @return
     */
    public static <T> T getClassByPath(String path) {
        try {
            return (T) Class.forName(path);
        } catch (ClassNotFoundException e) {
            logger.info(e.toString());
        }
        return null;
    }

    /**
     * 执行set方法
     *
     * @param o         执行对象
     * @param fieldName 属性名
     * @param value     值
     */
    public static void invokeSet(Object o, String fieldName, Object value) {
        try {
            Method method = getSetMethod(o.getClass(), fieldName);
            method.invoke(o, new Object[]{value});
        } catch (Exception e) {
            logger.info(e.toString());
        }
    }

    /**
     * 执行get方法
     *
     * @param o         执行对象
     * @param fieldName 属性名
     */
    public static Object invokeGet(Object o, String fieldName) {
        try {
            Method method = getGetMethod(o.getClass(), fieldName);
            return method.invoke(o, new Object[0]);
        } catch (Exception e) {
            logger.info(e.toString());
        }
        return null;
    }

    /**
     * java反射bean的set方法
     *
     * @param clazz     Class对象
     * @param fieldName 属性名
     * @return
     */
    public static Method getSetMethod(Class clazz, String fieldName) {
        try {
            Class[] parameterTypes = new Class[1];
            Field field = clazz.getDeclaredField(fieldName);
            parameterTypes[0] = field.getType();
            StringBuffer sb = new StringBuffer();
            sb.append("set");
            sb.append(fieldName.substring(0, 1).toUpperCase());
            sb.append(fieldName.substring(1));
            Method method = clazz.getMethod(sb.toString(), parameterTypes);
            return method;
        } catch (Exception e) {
            logger.info(e.toString());
        }
        return null;
    }

    /**
     * java反射bean的get方法
     *
     * @param clazz     Class对象
     * @param fieldName 属性名
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Method getGetMethod(Class clazz, String fieldName) {
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("get");
            sb.append(fieldName.substring(0, 1).toUpperCase());
            sb.append(fieldName.substring(1));
            return clazz.getMethod(sb.toString());
        } catch (Exception e) {
            logger.info(e.toString());
        }
        return null;
    }

    /**
     * 类转换,自定义方法
     *
     * @param obj   参数
     * @param clazz Class对象
     * @param <T>
     * @return
     */
    public static <T> T toClassByUtil(Object obj, Class<T> clazz) {
        try {
            Map map = (Map) obj;
            T t = clazz.newInstance();
            // 获取实体类的所有属性信息，返回Field数组
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                invokeSet(t, field.getName(), map.get(field.getName()));
            }
            return t;
        } catch (Exception e) {
            logger.info(e.toString());
        }
        return null;
    }

    /**
     * 类转换,fastjson方式
     *
     * @param obj   参数
     * @param clazz Class对象
     * @param <T>
     * @return
     */
    public static <T> T toClassByFastjson(Object obj, Class<T> clazz) {
        return JSON.parseObject(JSON.toJSONString(obj), clazz);
    }

//    public static void main(String[] args) {
//        ClassUtil.toClassByFastjson(null, null);
//        ClassUtil.toClassByUtil(null, null);
//    }
}
