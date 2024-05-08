package com.whn.example.provider;

import com.whn.example.common.service.UserService;
import com.whn.guazirpc.RpcApplication;
import com.whn.guazirpc.config.RegistryConfig;
import com.whn.guazirpc.config.RpcConfig;
import com.whn.guazirpc.model.ServiceMetaInfo;
import com.whn.guazirpc.registry.LocalRegistry;
import com.whn.guazirpc.registry.Registry;
import com.whn.guazirpc.registry.RegistryFactory;
import com.whn.guazirpc.server.tcp.VertxTcpServer;

/**
 * 服务提供者示例
 */
public class MultipleProvidersExample {
    public static void main(String[] args) {
        // RPC 框架初始化
        RpcApplication.init();

        // 注册服务到本都
        String serviceName = UserService.class.getName();
        LocalRegistry.registry(serviceName, UserServiceImpl.class);

        // 注册服务到注册中心
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo1 = new ServiceMetaInfo();
        serviceMetaInfo1.setServiceName(serviceName);
        serviceMetaInfo1.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo1.setServicePort(rpcConfig.getServerPort());
        ServiceMetaInfo serviceMetaInfo2 = new ServiceMetaInfo();
        serviceMetaInfo2.setServiceName(serviceName);
        serviceMetaInfo2.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo2.setServicePort(8120);
        try {
            registry.registry(serviceMetaInfo1);
            registry.registry(serviceMetaInfo2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 启动web服务
        VertxTcpServer vertxTcpServer = new VertxTcpServer();
        vertxTcpServer.doStart(RpcApplication.getRpcConfig().getServerPort());

    }
}
