# 瓜子RPC框架



## 项目介绍
基于Java、Vert.x、ETCD实现的高性能RPC框架。支持多种序列化器、注册中心、负载均衡、重试和容错机制。采用注解驱动设计，方便SpringBoot开发者使用。



## 使用说明

本框架支持SpringBoot开发者使用注解来使用RPC框架。

```java
@SpringBootApplication
@EnableRpc
public class ExampleSpringbootProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleSpringbootProviderApplication.class, args);
    }

}
```

使用`@EnableRpc`即可在项目启动时初始化框架

```java
@Service
@RpcService
public class UserServiceImpl implements UserService{


    @Override
    public User getUser(User user) {
        System.out.println("用户名：" + user.getName());
        return user;
    }
}
```

使用`@RpcService`即可将服务实现类注册为RPC服务

```java
@Service
public class ExampleServiceImpl {

    @RpcReference
    private UserService userService;

    public void test() {
        User user = new User();
        user.setName("kunkun");
        User resultUser = userService.getUser(user);
        System.out.println(resultUser.getName());
    }
}
```

服务调用法使用`@RpcReference`在服务接口类上即可注入RPC服务实现类。

项目支持`.properties`、`yml/yaml`配置文件来对RPC框架进行配置，且兼容`SpringBoot`的`application.yml`文件

```yaml
spring:
  application:
    name: guazi-rpc-consumer
rpc:
  name: guazi-rpc
  serverHost: localhost
  serverPort: 9999
  version: 1.0.0
  mock: false
  serializer: hessian
  loadBalancer: roundRobin
  retryStrategy: fixedInterval
  tolerantStrategy: failFast
  registryConfig:
    registry: etcd
    address: http://localhost:2380
```

