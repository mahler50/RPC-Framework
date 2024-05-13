package com.whn.guazirpc.springboot.strater.bootstrap;

import com.whn.guazirpc.proxy.ServiceProxy;
import com.whn.guazirpc.proxy.ServiceProxyFactory;
import com.whn.guazirpc.springboot.strater.annotation.RpcReference;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

/**
 * Rpc 服务消费者启动
 */
public class RpcConsumerBootstrap implements BeanPostProcessor {

    /**
     * Bean 初始化后，注入服务
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        Class<?> beanClass = bean.getClass();
        Field[] declaredFields = beanClass.getDeclaredFields();
        for (Field field :
                declaredFields) {
            RpcReference rpcReference = field.getAnnotation(RpcReference.class);
            if (rpcReference != null) {
                // 生成代理对象
                Class<?> interfaceClass = rpcReference.interfaceClass();
                if (interfaceClass == void.class) {
                    interfaceClass = field.getType();
                }
                field.setAccessible(true);
                Object proxyObject = ServiceProxyFactory.getProxy(interfaceClass);
                try {
                    field.set(bean, proxyObject);
                    field.setAccessible(false);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("为字段注入代理对象失败", e);
                }
            }
        }

        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
