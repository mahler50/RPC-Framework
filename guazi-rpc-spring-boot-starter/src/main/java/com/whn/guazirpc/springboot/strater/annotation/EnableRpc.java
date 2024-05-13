package com.whn.guazirpc.springboot.strater.annotation;


import com.whn.guazirpc.springboot.strater.bootstrap.RpcConsumerBootstrap;
import com.whn.guazirpc.springboot.strater.bootstrap.RpcInitBootstrap;
import com.whn.guazirpc.springboot.strater.bootstrap.RpcProviderBootstrap;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启动 Rpc 服务注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({RpcInitBootstrap.class, RpcProviderBootstrap.class, RpcConsumerBootstrap.class})
public @interface EnableRpc {

    /**
     * 需要启动 server
     * @return
     */
    boolean needServer() default true;
}
