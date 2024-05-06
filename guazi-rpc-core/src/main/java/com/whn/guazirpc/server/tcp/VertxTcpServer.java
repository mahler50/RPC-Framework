package com.whn.guazirpc.server.tcp;

import com.whn.guazirpc.server.HttpServer;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;

public class VertxTcpServer implements HttpServer {

    private byte[] handleRequest(byte[] requestData) {
        return "Hello Vertx".getBytes();
    }
    @Override
    public void doStart(int port) {
        // 创建Vertx实例
        Vertx vertx = Vertx.vertx();

        // 创建TCP服务器
        NetServer server = vertx.createNetServer();

        // 处理请求
        server.connectHandler(new TcpServerHandler());

        // 绑定服务端口
        server.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("TCP server started on port:" + port);
            } else {
                System.out.println("failed to start TCP server" + result.cause());
            }
        });
    }

    public static void main(String[] args) {
        new VertxTcpServer().doStart(8888);
    }

}
