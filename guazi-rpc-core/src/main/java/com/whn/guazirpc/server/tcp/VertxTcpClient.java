package com.whn.guazirpc.server.tcp;

import cn.hutool.core.util.IdUtil;
import com.whn.guazirpc.RpcApplication;
import com.whn.guazirpc.model.RpcRequest;
import com.whn.guazirpc.model.RpcResponse;
import com.whn.guazirpc.model.ServiceMetaInfo;
import com.whn.guazirpc.protocol.*;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Vertx TCP 客户端
 */
public class VertxTcpClient {

    /**
     * 发送请求
     * @param rpcRequest
     * @param serviceMetaInfo
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static RpcResponse doRequest(RpcRequest rpcRequest, ServiceMetaInfo serviceMetaInfo) throws ExecutionException, InterruptedException {
        // 发送TCP请求
        Vertx vertx = Vertx.vertx();
        NetClient netClient = vertx.createNetClient();
        CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();
        netClient.connect(serviceMetaInfo.getServicePort(),
                serviceMetaInfo.getServiceHost(),
                result -> {
                    if (result.succeeded()) {
                        System.out.println("Connected to TCP server");
                        NetSocket socket = result.result();
                        // 发送消息
                        // 构造消息
                        ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
                        ProtocolMessage.Header header = new ProtocolMessage.Header();
                        header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
                        header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
                        header.setSerializer((byte)
                                ProtocolMessageSerializerEnum.getEnumByValue(RpcApplication.getRpcConfig().getSerializer()).getKey());
                        header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
                        header.setStatus((byte) ProtocolStatusEnum.OK.getValue());
                        // 生成全局请求 Id
                        header.setRequestId(IdUtil.getSnowflakeNextId());
                        protocolMessage.setHeader(header);
                        protocolMessage.setBody(rpcRequest);
                        // 编码请求
                        try {
                            Buffer encode = ProtocolMessageEncoder.encode(protocolMessage);
                            socket.write(encode);
                        } catch (IOException e) {
                            throw new RuntimeException("协议消息编码错误");
                        }

                        // 接收响应
                        socket.handler(buffer -> {
                            try {
                                ProtocolMessage<RpcResponse> rpcResponseProtocolMessage =
                                        (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
                                responseFuture.complete(rpcResponseProtocolMessage.getBody());
                            } catch (IOException e) {
                                throw new RuntimeException("协议消息解码错误");
                            }
                        });
                    } else {
                        System.out.println("Failed to connect to TCP server");
                    }
                });
        // todo 可能导致Vertx报 Thread blocked异常，需优化
        RpcResponse rpcResponse = responseFuture.get();
        // 关闭连接
        netClient.close();
        return rpcResponse;
    }
}
