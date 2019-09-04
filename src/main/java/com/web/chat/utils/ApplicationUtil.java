package com.web.chat.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 接口调用映射
 *
 * @param <T>
 */
@Component
public class ApplicationUtil<T> implements ApplicationContextAware {
    private static Logger logger = LoggerFactory.getLogger(Object.class);
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 通过反射执行
     *
     * @param clazz      Class对象
     * @param methodName 方法名
     * @param obj        参数
     * @return
     */
    public static Object execute(Class clazz, String methodName, Object... obj) {
        try {
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (methodName.equals(method.getName())) {
                    return method.invoke(clazz.newInstance(), obj);
                }
            }
        } catch (Exception e) {
            logger.info(e.toString());
        }
        return null;
    }

//    public static void main(String[] args) {
//        ApplicationUtil.execute(Object.class, "login");
//        ApplicationUtil.execute(Object.class, "login", new Object());
//    }
}