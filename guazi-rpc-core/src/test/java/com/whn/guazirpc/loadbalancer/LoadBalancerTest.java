package com.whn.guazirpc.loadbalancer;

import com.whn.guazirpc.model.ServiceMetaInfo;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * 负载均衡器测试
 */
public class LoadBalancerTest {

    final LoadBalancer loadBalancer = new ConsistentHashLoadBalancer();

    @Test
    public void select() {
        // 请求参数
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("methodName", "apple");

        ServiceMetaInfo serviceMetaInfo1 = new ServiceMetaInfo();
        serviceMetaInfo1.setServiceName("myService");
        serviceMetaInfo1.setServiceVersion("1.0");
        serviceMetaInfo1.setServiceHost("localhost");
        serviceMetaInfo1.setServicePort(1234);
        ServiceMetaInfo serviceMetaInfo2 = new ServiceMetaInfo();
        serviceMetaInfo2.setServiceName("myService");
        serviceMetaInfo2.setServiceVersion("1.0");
        serviceMetaInfo2.setServiceHost("kunkun.icu");
        serviceMetaInfo2.setServicePort(80);

        List<ServiceMetaInfo> serviceMetaInfoList = Arrays.asList(serviceMetaInfo1, serviceMetaInfo2);

        ServiceMetaInfo selected = loadBalancer.select(requestParams, serviceMetaInfoList);
        System.out.println(selected);
        Assert.assertNotNull(selected);
        selected = loadBalancer.select(requestParams, serviceMetaInfoList);
        System.out.println(selected);
        Assert.assertNotNull(selected);
        selected = loadBalancer.select(requestParams, serviceMetaInfoList);
        System.out.println(selected);
        Assert.assertNotNull(selected);
    }
}