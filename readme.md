### 1. SpringCloudAlibaba组件说明
```
1. nacos            服务注册中心、配置中心、数据总线，单机启动：startup.cmd -m standalone
2. sentinel         服务熔断降级
3. ribbon/openfeign         服务远程调用
4. gateway          服务网关
5. seata            分布式事务
```
### 2. 模块说明
```
1. springcloud-commons          公共模块，包含SpringSecurity的token校验过滤器、Ribbon/OpenFeign服务间调用请求头丢失问题的解决
2. springcloud-authentication-center         认证中心（SpringSecurity+jwt+redis，整合自：https://gitee.com/iefihz/springsecurity-jwt-demo/tree/feature-jwt-redis/）
3. springcloud-gateway          网关微服务，请求入口
4. springcloud-order            订单微服务，消费storage的服务
5. springcloud-storage          库存微服务，被order调用的服务
```