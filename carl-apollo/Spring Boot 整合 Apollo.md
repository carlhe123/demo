> 本文主要介绍Spring boot整合Apollo。



### 一、环境准备

#### 1.1、工具版本信息

jdk 1.8

spring boot 2.1.11.RELEASE

apollo server 1.6.1

apollo client 1.6.0

#### 1.2、pom.xml

```java
<dependency>
    <groupId>com.ctrip.framework.apollo</groupId>
    <artifactId>apollo-client</artifactId>
    <version>1.6.0</version>
</dependency>
```



### 二、Apollo界面操作

#### 2.1、用户管理

![1593503452576](C:\Users\hebiao\AppData\Roaming\Typora\typora-user-images\1593503452576.png)

![1593503464448](C:\Users\hebiao\AppData\Roaming\Typora\typora-user-images\1593503464448.png)



#### 2.2、创建项目

![1593503321418](C:\Users\hebiao\AppData\Roaming\Typora\typora-user-images\1593503321418.png)

![1593503544988](C:\Users\hebiao\AppData\Roaming\Typora\typora-user-images\1593503544988.png)

> `注：配置负责人和管理员后只有指定的账号或超级管理员可以修改该项目的配置，从而进行权限管控。`

#### 2.3、添加配置

点击新增配置，填写key:value

![1593503931002](C:\Users\hebiao\AppData\Roaming\Typora\typora-user-images\1593503931002.png)

![1593504066228](C:\Users\hebiao\AppData\Roaming\Typora\typora-user-images\1593504066228.png)

`注：新增配置后需要点击发布使未发布的配置生效.`

![1593504157352](C:\Users\hebiao\AppData\Roaming\Typora\typora-user-images\1593504157352.png)



#### 2.4、删除应用、集群、namespace

如应用等需要删除，点击管理员工具

![1593504283285](C:\Users\hebiao\AppData\Roaming\Typora\typora-user-images\1593504283285.png)

`注：只有超级管理员有该权限，需谨慎操作。`



### 三、使用Apollo的必要配置

#### 3.1、appId 

AppId是应用的身份信息，是从服务端获取配置的一个重要信息。

有以下几种方式设置，按照优先级从高到低分别为：

1.System Property

Apollo 0.7.0+支持通过System Property传入app.id信息，如

```properties
-Dapp.id=YOUR-APP-ID
```

2.操作系统的System Environment

Apollo 1.4.0+支持通过操作系统的System Environment `APP_ID`来传入app.id信息，如

```properties
APP_ID=YOUR-APP-ID
```

3.Spring Boot application.properties

Apollo 1.0.0+支持通过Spring Boot的application.properties文件配置，如

```properties
app.id=YOUR-APP-ID
```

> 该配置方式不适用于多个war包部署在同一个tomcat的使用场景

4.app.properties

确保classpath:/META-INF/app.properties文件存在，并且其中内容形如：

> app.id=YOUR-APP-ID

文件位置参考如下：

![1593505383545](C:\Users\hebiao\AppData\Roaming\Typora\typora-user-images\1593505383545.png)

> 注：app.id是用来标识应用身份的唯一id，格式为string。

#### 3.2、Apollo Meta Server

Apollo支持应用在不同的环境有不同的配置，所以需要在运行提供给Apollo客户端当前环境的[Apollo Meta Server](https://github.com/ctripcorp/apollo/wiki/Apollo配置中心设计#133-meta-server)信息。默认情况下，meta server和config service是部署在同一个JVM进程，所以meta server的地址就是config service的地址。

为了实现meta server的高可用，推荐通过SLB（Software Load Balancer）做动态负载均衡。Meta server地址也可以填入IP，如`http://1.1.1.1:8080,http://2.2.2.2:8080`，不过生产环境还是建议使用域名（走slb），因为机器扩容、缩容等都可能导致IP列表的变化。

1.0.0版本开始支持以下方式配置apollo meta server信息，按照优先级从高到低分别为：

1. 通过Java System Property `apollo.meta`

   - 可以通过Java的System Property `apollo.meta`来指定

   - 在Java程序启动脚本中，可以指定

     ```properties
     -Dapollo.meta=http://config-service-url
     ```

     - 如果是运行jar文件，需要注意格式是`java -Dapollo.meta=http://config-service-url -jar xxx.jar`

   - 也可以通过程序指定，如`System.setProperty("apollo.meta", "http://config-service-url");`

2. 通过Spring Boot的配置文件

   - 可以在Spring Boot的`application.properties`或`bootstrap.properties`中指定`apollo.meta=http://config-service-url`

   >  该配置方式不适用于多个war包部署在同一个tomcat的使用场景

3. 通过操作系统的System Environment `APOLLO_META`

   - 可以通过操作系统的System Environment `APOLLO_META`来指定
   - 注意key为全大写，且中间是`_`分隔

4. 通过`server.properties`配置文件

   - 可以在`server.properties`配置文件中指定`apollo.meta=http://config-service-url`

   - 对于Mac/Linux，文件位置为`/opt/settings/server.properties`

   - 对于Windows，文件位置为`C:\opt\settings\server.properties`

     ![1593506316003](C:\Users\hebiao\AppData\Roaming\Typora\typora-user-images\1593506316003.png)

     文件内容：

     ```properties
     #环境配置
     env=UAT 
     #meta server配置
     apollo.meta=http://172.16.81.188:8080
     ```

5. 通过`app.properties`配置文件

   - 可以在`classpath:/META-INF/app.properties`指定`apollo.meta=http://config-service-url`

   ```properties
   #appId配置
   app.id=apollo-demo
   #meta server配置
   apollo.meta=http://172.16.81.188:8080
   ```

6. 通过Java system property `${env}_meta`

   - 如果当前[env](https://github.com/ctripcorp/apollo/wiki/Java客户端使用指南#1241-environment)是`dev`，那么用户可以配置`-Ddev_meta=http://config-service-url`
   - 使用该配置方式，那么就必须要正确配置Environment，详见 3.3、Environment配置

7. 通过操作系统的System Environment `${ENV}_META` (1.2.0版本开始支持)

   - 如果当前[env](https://github.com/ctripcorp/apollo/wiki/Java客户端使用指南#1241-environment)是`dev`，那么用户可以配置操作系统的System Environment `DEV_META=http://config-service-url`
   - 注意key为全大写
   - 使用该配置方式，那么就必须要正确配置Environment，详见 3.3、Environment配置

8. 通过`apollo-env.properties`文件

   - 用户也可以创建一个`apollo-env.properties`，放在程序的classpath下(与application.yml/application.properties同一级)，或者放在spring boot应用的config目录下(`未验证成功`)
   - 使用该配置方式，那么就必须要正确配置Environment，详见 3.3、Environment配置
   - 文件内容形如：

```properties
# Eureka / config server / meta server 地址为同一个
dev.meta=http://172.16.81.188:8080
#fat.meta=http://172.16.81.188:8080
#uat.meta=http://172.16.81.188:8081
#pro.meta=http://172.16.81.188:8080
```

> 如果通过以上各种手段都无法获取到Meta Server地址，Apollo最终会fallback到`http://apollo.meta`作为Meta Server地址

#### 3.3、Environment

Environment可以通过以下3种方式的任意一个配置：

1. 通过Java System Property

   - 可以通过Java的System Property `env`来指定环境

   - 在Java程序启动脚本中，可以指定

     ```
     -Denv=YOUR-ENVIRONMENT
     ```

     - 如果是运行jar文件，需要注意格式是`java -Denv=YOUR-ENVIRONMENT -jar xxx.jar`

   - 注意key为全小写

2. 通过操作系统的System Environment

   - 还可以通过操作系统的System Environment `ENV`来指定
   - 注意key为全大写

3. 通过配置文件

   - 最后一个推荐的方式是通过配置文件来指定`env=YOUR-ENVIRONMENT`
   - 对于Mac/Linux，文件位置为`/opt/settings/server.properties`
   - 对于Windows，文件位置为`C:\opt\settings\server.properties`

文件内容形如：

```
env=DEV
```

目前，`env`支持以下几个值（大小写不敏感）：

- DEV
  - Development environment
- FAT
  - Feature Acceptance Test environment
- UAT
  - User Acceptance Test environment
- PRO
  - Production environment

更多环境定义，可以参考[Env.java](https://github.com/ctripcorp/apollo/blob/master/apollo-core/src/main/java/com/ctrip/framework/apollo/core/enums/Env.java)

#### 3.4、添加@EnableApolloConfig注解

```java
@SpringBootApplication
@EnableApolloConfig
//@MapperScan(basePackages = {"com.carl.apollo.mapper"})
public class CarlApolloApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarlApolloApplication.class, args);
    }

}
```



### 四、环境切换

#### 1、使用`apollo-env.properties`和`server.properties`来切换环境

apollo-env.properties

```properties
# Eureka / config server / meta server 地址为同一个
dev.meta=http://172.16.81.188:8080
#fat.meta=http://172.16.81.188:8080
uat.meta=http://172.16.81.188:8081
#pro.meta=http://172.16.81.188:8080
```

C:\opt\settings\server.properties

```properties
env=DEV #client会取dev环境的配置，即http://172.16.81.188:8080对应环境的配置。
#env=UAT #client会取uat环境的配置，即http://172.16.81.188:8081对应环境的配置。
```

`注：项目启动时，apollo会通过SPI（LegacyMetaServerProvider.java/DefaultPortalMetaServerProvider）来加载meta server信息，client可自定义实现。`



#### 2、使用spring profiles来实现多环境切换

application.yml

```yml
spring:
  profiles:
    active: dev # 激活application-dev.yml配置
```

application-dev.yml

```yaml
app:
  id: carl-apollo
apollo:
  meta: http://172.16.81.188:8080
  bootstrap:
    enabled: true
    eagerLoad:
      enabled: true
```

application-uat.yml

```yaml
app:
  id: carl-apollo
apollo:
  meta: http://172.16.81.188:8081
  bootstrap:
    enabled: true
    eagerLoad:
      enabled: true
```

