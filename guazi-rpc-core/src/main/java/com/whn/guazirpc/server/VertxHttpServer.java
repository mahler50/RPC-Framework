package com.whn.guazirpc.server;

import io.vertx.core.Vertx;

/**
 * Vertx HTTP 服务器
 */
public class VertxHttpServer implements HttpServer{
    @Override
    public void doStart(int port) {
        // 创建Vertx实例
        Vertx vertx = Vertx.vertx();
        // 创建服务器实例
        vertx.createHttpServer()
                .requestHandler(new HttpServerHandler())
                // 启动HTTP服务器并监听端口
                .listen(port, result -> {
                    if (result.succeeded()) {
                        System.out.println("Server is now listened on port" + port);
                    } else {
                        System.out.println("Failed to start server:" +result.cause());
                    }
                });
    }
}
