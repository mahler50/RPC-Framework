package com.whn.guazirpc.proxy;

import cn.hutool.core.collection.CollUtil;
import com.whn.guazirpc.RpcApplication;
import com.whn.guazirpc.config.RpcConfig;
import com.whn.guazirpc.constant.RpcConstant;
import com.whn.guazirpc.fault.retry.RetryStrategy;
import com.whn.guazirpc.fault.retry.RetryStrategyFactory;
import com.whn.guazirpc.fault.tolerant.TolerantStrategy;
import com.whn.guazirpc.fault.tolerant.TolerantStrategyFactory;
import com.whn.guazirpc.loadbalancer.LoadBalancer;
import com.whn.guazirpc.loadbalancer.LoadBalancerFactory;
import com.whn.guazirpc.model.RpcRequest;
import com.whn.guazirpc.model.RpcResponse;
import com.whn.guazirpc.model.ServiceMetaInfo;
import com.whn.guazirpc.registry.Registry;
import com.whn.guazirpc.registry.RegistryFactory;
import com.whn.guazirpc.serializer.Serializer;
import com.whn.guazirpc.serializer.SerializerFactory;
import com.whn.guazirpc.server.tcp.VertxTcpClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务代理
 */
public class ServiceProxy implements InvocationHandler {

    /**
     * 调用代理
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 指定序列化容器
        final Serializer serializer =
                SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        // 构造请求
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        try {
            // 从注册中心获取服务提供者地址
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList =
                    registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)) {
                throw new RuntimeException("暂无服务地址");
            }

            // 负载均衡
            LoadBalancer loadBalancer
                    = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
            // 将调用方法名作为负载均衡参数
            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put("methodName", rpcRequest.getMethodName());
            ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);
            // 发送 TCP 请求
            // 使用重试机制
            RpcResponse rpcResponse;
            try {
                RetryStrategy retryStrategy
                        = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
                rpcResponse = retryStrategy.doRetry(() ->
                        VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo)
                );
            } catch (Exception e) {
                // 容错策略
                TolerantStrategy tolerantStrategy
                        = TolerantStrategyFactory.getInstance(rpcConfig.getTolerantStrategy());
                rpcResponse = tolerantStrategy.doTolerant(null, e);
            }

            return rpcResponse.getData();
        } catch (Exception e) {
            throw new RuntimeException("调用失败");
        }
    }
}
