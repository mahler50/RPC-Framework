package com.whn.guazirpc.bootstrap;

import com.whn.guazirpc.RpcApplication;
import com.whn.guazirpc.config.RegistryConfig;
import com.whn.guazirpc.config.RpcConfig;
import com.whn.guazirpc.model.ServiceMetaInfo;
import com.whn.guazirpc.model.ServiceRegisterInfo;
import com.whn.guazirpc.registry.LocalRegistry;
import com.whn.guazirpc.registry.Registry;
import com.whn.guazirpc.registry.RegistryFactory;
import com.whn.guazirpc.server.tcp.VertxTcpServer;

import java.util.List;

/**
 * 服务提供者初始化
 */
public class ProviderBootstrap {

    public static void init(List<ServiceRegisterInfo<?>> serviceRegisterInfoList) {
        // RPC 框架初始化
        RpcApplication.init();
        // 全局配置
        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        for (ServiceRegisterInfo<?> serviceRegisterInfo :
              serviceRegisterInfoList  ) {
            String serviceName = serviceRegisterInfo.getServiceName();
            // 注册服务到本都
            LocalRegistry.registry(serviceName, serviceRegisterInfo.getImplClass());
            // 注册服务到注册中心
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            try {
                registry.registry(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        // 启动web服务
        VertxTcpServer vertxTcpServer = new VertxTcpServer();
        vertxTcpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
