package com.xsmart.camera.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class BeanFactory implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);
    private static ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        applicationContext = ac;  
    }  
      
    public static ApplicationContext getApplicationContext(){
        return applicationContext;  
    }

    public static Object lookUp(String bean){
        Object obj = null;
        try {
           obj = applicationContext.getBean(bean);
        } catch (BeansException e) {
            logger.error("BeanFactory cannot find this bean,beanname is:"+bean,e);
        }
        return obj;
    }
    public static <T> T lookUp(String bean,Class<T> className){
        T obj = null;
        try {
            obj = applicationContext.getBean(bean,className);
        } catch (BeansException e) {
            logger.error("BeanFactory cannot find this bean,class is:"+className.getClass()+"and bean name is:"+bean,e);
        }
       return applicationContext.getBean(bean,className);
    }
    public static <T> T lookUp(Class<T> className){
        T obj = null;
        try {
            obj = applicationContext.getBean(className);
        } catch (BeansException e) {
            logger.error("BeanFactory cannot find this bean,class is:"+className.getClass(),e);
        }
        return obj;
    }
}
