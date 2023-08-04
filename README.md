# My-RPC
基于 netty开发的框架，目前支持 zookeeper/nacos 注册中心、kryo/java 序列化、 可配置的轮询和随机算法负载均衡 ，借鉴了 Dubbo 的 SPI，可以通过配置文件进行配置，为之后扩展注册中心和序列化框架提供了便捷。

## 1、快速开始

### 0. 启动注册中心

这里使用的注册中心是 nacos-2.2.3

### 1.1 服务端

1. 引入 MyRPC-core 

2. 创建 rpc.properties 配置文件,进行如下配置：

   ```properties 
   discovery=nacos
   registrarAddress=localhost:8848
   serialize=kryo
   loadbalance=random
   port=1234
   ```
   可选配置：

   | 配置项           | 类型   | 可选值          | 可以为空 | 默认值 |
   | ---------------- | ------ | --------------- | -------- | ------ |
   | discovery        | string | nacos/zookeeper | ×        | -      |
   | registrarAddress | string | -               | ×        | -      |
   | serialize        | string | kryo/java       | ×        | -      |
   | loadbalance      | string | random/rotate   | ×        | -      |
   | port             | number | -               | √        | 1017   |

3. 在项目的启动类加注解 @RpcScan(packages = {"*"})

   ```java
   @RpcScan(packages = {"com.zlgewj"})
   public class ServerApplication {
       public static void main(String[] args) throws InterruptedException {
           RpcStarter.open(ServerApplication.class);
       }
   }
   ```

   

4. 定义服务接口

   ```java
   public interface HelloService {
       String sayHello(String name);
   
       List<User> getFriends(User user);
   }
   ```

5. 实现服务接口，标注 @RpcService 注解（name 字段是必需的，client 和 server 会通过 service 的 name、version、group 来定位唯一的service）

   ```java
   @RpcService(name = "hello")
   public class HelloServiceImpl implements HelloService {
       @Override
       public String sayHello(String name) {
           return "hello,"+name + "!!!!!!";
       }
   
       @Override
       public List<User> getFriends(User user) {
           int a = 1/0;
           return user.getFriends();
       }
   }
   
   ```

   

6. 调用 open() 方法

   ```java
   @RpcScan(packages = {"com.zlgewj"})
   public class ServerApplication {
       public static void main(String[] args) throws InterruptedException {
           RpcStarter.open(ServerApplication.class);
       }
   }
   ```

### 1.2 客户端

1. 引入 MyRPC-core

2. 创建配置文件并进行配置

   ```properties
   discovery=nacos
   registrarAddress=localhost:8848
   serialize=kryo
   loadbalance=random
   port=1234
   ```

3. 在项目的启动类加注解 @RpcScan(packages = {"*"})

   ```java
   @RpcScan(packages = {"com.zlgewj"})
   public class ClientApplication {
       public static void main(String[] args) {
           AnnotationConfigApplicationContext annotationContext = new AnnotationConfigApplicationContext(ClientApplication.class);
           TestController controller = (TestController) annotationContext.getBean("testController");
           controller.test();
       }
   }
   ```

4. 把服务接口注入到调用方(`@RpcReference` 注解的 serviceName 应该与 `@RpcService` 的 name 对应，version 和 group 默认值为 "1,0,0","default")

   ```java
   @Component
   public class TestController {
   
       @RpcReference(serviceName = "hello")
       HelloService helloService;
       public String test() {
           String hello = helloService.sayHello("zhangsan");
           return hello;
       }
   }
   ```
