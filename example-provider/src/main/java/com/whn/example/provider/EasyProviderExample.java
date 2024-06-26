package com.whn.example.provider;

import com.whn.example.common.service.UserService;
import com.whn.guazirpc.RpcApplication;
import com.whn.guazirpc.registry.LocalRegistry;
import com.whn.guazirpc.server.HttpServer;
import com.whn.guazirpc.server.VertxHttpServer;

/**
 * 简单服务提供者示例
 */
public class EasyProviderExample {
    public static void main(String[] args) {
        // RPC 框架初始化
        RpcApplication.init();

        // 注册服务
        LocalRegistry.registry(UserService.class.getName(), UserServiceImpl.class);

        // 启动web服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());

    }
}
