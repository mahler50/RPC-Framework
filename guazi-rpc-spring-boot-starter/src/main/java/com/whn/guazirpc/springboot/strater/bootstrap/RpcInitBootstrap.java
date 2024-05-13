package com.whn.guazirpc.springboot.strater.bootstrap;

import com.whn.guazirpc.RpcApplication;
import com.whn.guazirpc.config.RpcConfig;
import com.whn.guazirpc.server.tcp.VertxTcpServer;
import com.whn.guazirpc.springboot.strater.annotation.EnableRpc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Rpc 框架启动
 */
@Slf4j
public class RpcInitBootstrap implements ImportBeanDefinitionRegistrar {
    /**
     * Spring 初始化时执行，初始化 Rpc 框架
     * @param importingClassMetadata
     * @param registry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 获取 EnableRpc 注解的属性值
        boolean needServer = (boolean)
                importingClassMetadata.getAnnotationAttributes(EnableRpc.class.getName())
                .get("needServer");

        // Rpc 框架初始化
        RpcApplication.init();

        // 全局配置
        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        // 启动服务器
        if (needServer) {
            VertxTcpServer vertxTcpServer = new VertxTcpServer();
            vertxTcpServer.doStart(rpcConfig.getServerPort());
        } else {
            log.info("不启动 server");
        }
    }
}
