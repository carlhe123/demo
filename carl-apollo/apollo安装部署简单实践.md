> 本文主要介绍apollo的安装和部署。



### 一、环境准备

#### 1.1、运行时环境

##### 1.1.1 OS

​		服务端基于Spring Boot，启动脚本理论上支持所有Linux发行版，建议CentOS 7。

##### 1.1.2 Java

- Apollo服务端：1.8+
- Apollo客户端：1.7+

由于需要同时运行服务端和客户端，所以建议安装Java 1.8+。

> 对于Apollo客户端，运行时环境只需要1.7+即可。

> 注：对于Apollo客户端，如果有需要的话，可以做少量代码修改来降级到Java 1.6，详细信息可以参考Issue 483

在配置好后，可以通过如下命令检查：

```
java -version
```

样例输出：

```
java version "1.8.0_74"
Java(TM) SE Runtime Environment (build 1.8.0_74-b02)
Java HotSpot(TM) 64-Bit Server VM (build 25.74-b02, mixed mode)
```

#### 1.2 MySQL

- 版本要求：5.6.5+

  Apollo的表结构对timestamp使用了多个default声明，所以需要5.6.5以上版本。

连接上MySQL后，可以通过如下命令检查：

```
SHOW VARIABLES WHERE Variable_name = 'version';
```

| Variable_name | Value  |
| ------------- | ------ |
| version       | 5.7.11 |

#### 1.3 环境

​		分布式部署需要事先确定部署的环境以及部署方式。

Apollo目前支持以下环境：

- DEV
  - 开发环境
- FAT
  - 测试环境，相当于alpha环境(功能测试)
- UAT
  - 集成环境，相当于beta环境（回归测试）
- PRO
  - 生产环境

### 二、部署步骤

#### 2.1、下载源码

```bash
git clone https://github.com/ctripcorp/apollo.git
```

![1593567242127](C:\Users\hebiao\AppData\Roaming\Typora\typora-user-images\1593567242127.png)

#### 2.1、创建数据库

​		Apollo服务端共需要两个数据库：`ApolloPortalDB`和`ApolloConfigDB`，我们把数据库、表的创建和样例数据都分别准备了sql文件，只需要导入数据库即可。

​		需要注意的是ApolloPortalDB只需要在生产环境部署一个即可，而ApolloConfigDB需要在每个环境部署一套，如fat、uat和pro分别部署3套ApolloConfigDB。

> 注意：如果你本地已经创建过Apollo数据库，请注意备份数据。我们准备的sql文件会清空Apollo相关的表。

利用工具导入源码中如下两个文件即可：

![1593567322918](C:\Users\hebiao\AppData\Roaming\Typora\typora-user-images\1593567322918.png)

##### 2.1.1、调整ApolloPortalDB配置

![1593567873271](C:\Users\hebiao\AppData\Roaming\Typora\typora-user-images\1593567873271.png)

###### 2.1.1.1、apollo.portal.envs - 可支持的环境列表

​	默认值是dev，如果portal需要管理多个环境的话，以逗号分隔即可（大小写不敏感），如：

```properties
DEV,FAT,UAT,PRO
```

修改完需要重启生效。

###### 2.1.1.2、apollo.portal.meta.servers - 各环境Meta Service列表

> 适用于1.6.0及以上版本

Apollo Portal需要在不同的环境访问不同的meta service(apollo-configservice)地址，所以我们需要在配置中提供这些信息。默认情况下，meta service和config service是部署在同一个JVM进程，所以meta service的地址就是config service的地址。

样例如下：

```json
{
    "DEV":"http://1.1.1.1:8080",
    "FAT":"http://apollo.fat.xxx.com",
    "UAT":"http://apollo.uat.xxx.com",
    "PRO":"http://apollo.xxx.com"
}
```

修改完需要重启生效。

> 该配置优先级高于其它方式设置的Meta Service地址。

###### 2.1.1.3、organizations - 部门列表

Portal中新建的App都需要选择部门，所以需要在这里配置可选的部门信息，样例如下：

```
[{"orgId":"TEST1","orgName":"样例部门1"},{"orgId":"TEST2","orgName":"样例部门2"}]
```

> 其他配置信息可参考apollo官方部署文档[分布式部署指南](https://github.com/ctripcorp/apollo/wiki/分布式部署指南)

##### 2.1.2、调整ApolloConfigDB配置

​		配置项统一存储在ApolloConfigDB.ServerConfig表中，需要注意每个环境的ApolloConfigDB.ServerConfig都需要单独配置，修改完一分钟实时生效。

![1593568392049](C:\Users\hebiao\AppData\Roaming\Typora\typora-user-images\1593568392049.png)

###### 2.1.2.1、eureka.service.url - Eureka服务Url

​		不管是apollo-configservice还是apollo-adminservice都需要向eureka服务注册，所以需要配置eureka服务地址。 按照目前的实现，apollo-configservice本身就是一个eureka服务，所以只需要填入apollo-configservice的地址即可，如有多个，用逗号分隔（注意不要忘了/eureka/后缀）。

​		需要注意的是每个环境只填入自己环境的eureka服务地址，比如

FAT的apollo-configservice是1.1.1.1:8080和2.2.2.2:8080，

UAT的apollo-configservice是3.3.3.3:8080和4.4.4.4:8080，

PRO的apollo-configservice是5.5.5.5:8080和6.6.6.6:8080，

那么：

1. 在FAT环境的ApolloConfigDB.ServerConfig表中设置eureka.service.url为：

```
http://1.1.1.1:8080/eureka/,http://2.2.2.2:8080/eureka/
```

2. 在UAT环境的ApolloConfigDB.ServerConfig表中设置eureka.service.url为：

```
http://3.3.3.3:8080/eureka/,http://4.4.4.4:8080/eureka/
```

3. 在PRO环境的ApolloConfigDB.ServerConfig表中设置eureka.service.url为：

```
http://5.5.5.5:8080/eureka/,http://6.6.6.6:8080/eureka/
```

> 注1：这里需要填写本环境中全部的eureka服务地址，因为eureka需要互相复制注册信息
>
> 注2：其他配置信息可参考apollo官方部署文档[分布式部署指南](https://github.com/ctripcorp/apollo/wiki/分布式部署指南)



#### 2.2、虚拟机部署

##### 2.2.1、获取安装包

可以通过两种方式获取安装包：

1. 直接下载安装包
   - 从[GitHub Release](https://github.com/ctripcorp/apollo/releases)页面下载预先打好的安装包
   - 如果对Apollo的代码没有定制需求，建议使用这种方式，可以省去本地打包的过程
2. 通过源码构建
   - 从[GitHub Release](https://github.com/ctripcorp/apollo/releases)页面下载Source code包或直接clone[源码](https://github.com/ctripcorp/apollo)后在本地构建
   - 如果需要对Apollo的做定制开发，需要使用这种方式



###### 2.2.1.1、直接下载安装包

1）获取apollo-configservice、apollo-adminservice、apollo-portal安装包

​		从[GitHub Release](https://github.com/ctripcorp/apollo/releases)页面下载最新版本的`apollo-configservice-x.x.x-github.zip`、`apollo-adminservice-x.x.x-github.zip`和`apollo-portal-x.x.x-github.zip`即可。



2）修改数据库连接信息

​		Apollo服务端需要知道如何连接到你前面创建的数据库，数据库连接串信息位于上一步下载的压缩包中的`config/application-github.properties`中。

1. 解压`apollo-configservice-x.x.x-github.zip`
2. 用程序员专用编辑器（如vim，notepad++，sublime等）打开`config`目录下的`application-github.properties`文件
3. 填写正确的ApolloConfigDB数据库连接串信息，注意用户名和密码后面不要有空格!
4. 修改完的效果如下：

```properties
# DataSource
spring.datasource.url = jdbc:mysql://localhost:3306/xxx?useSSL=false&characterEncoding=utf8
spring.datasource.username = someuser
spring.datasource.password = somepwd
```

> 注：由于ApolloConfigDB在每个环境都有部署，所以对不同的环境config-service需要配置对应环境的数据库参数



3）配置apollo-portal的meta service信息

​		Apollo Portal需要在不同的环境访问不同的meta service(apollo-configservice)地址，所以我们需要在配置中提供这些信息。默认情况下，meta service和config service是部署在同一个JVM进程，所以meta service的地址就是config service的地址。

> 对于1.6.0及以上版本，可以通过ApolloPortalDB.ServerConfig中的配置项来配置Meta Service地址。

使用程序员专用编辑器（如vim，notepad++，sublime等）打开`apollo-portal-x.x.x-github.zip`中`config`目录下的`apollo-env.properties`文件。

假设

DEV的apollo-configservice未绑定域名，地址是1.1.1.1:8080，

FAT的apollo-configservice绑定了域名apollo.fat.xxx.com，

UAT的apollo-configservice绑定了域名apollo.uat.xxx.com，

PRO的apollo-configservice绑定了域名apollo.xxx.com，

那么可以如下修改各环境meta service服务地址，格式为`${env}.meta=http://${config-service-url:port}`，如果某个环境不需要，也可以直接删除对应的配置项（如lpt.meta）：

```
dev.meta=http://1.1.1.1:8080
fat.meta=http://apollo.fat.xxx.com
uat.meta=http://apollo.uat.xxx.com
pro.meta=http://apollo.xxx.com
```

除了通过`apollo-env.properties`方式配置meta service以外，apollo也支持在运行时指定meta service（优先级比`apollo-env.properties`高）：

1. 通过Java System Property

    

   ```
   ${env}_meta
   ```

   - 可以通过Java的System Property `${env}_meta`来指定
   - 如`java -Ddev_meta=http://config-service-url -jar xxx.jar`
   - 也可以通过程序指定，如`System.setProperty("dev_meta", "http://config-service-url");`

2. 通过操作系统的System Environment

   ```
   ${ENV}_META
   ```

   - 如`DEV_META=http://config-service-url`
   - 注意key为全大写，且中间是`_`分隔

> 注1: 为了实现meta service的高可用，推荐通过SLB（Software Load Balancer）做动态负载均衡

> 注2: meta service地址也可以填入IP，0.11.0版本之前只支持填入一个IP。从0.11.0版本开始支持填入以逗号分隔的多个地址，如`http://1.1.1.1:8080,http://2.2.2.2:8080`，不过生产环境还是建议使用域名（走slb），因为机器扩容、缩容等都可能导致IP列表的变化。



###### 2.2.1.2 通过源码构建

1）修改数据库连接信息

​		Apollo服务端需要知道如何连接到你前面创建的数据库，所以需要编辑linux：scripts/build.sh，Windows：scripts/build.bat，修改ApolloPortalDB和ApolloConfigDB相关的数据库连接串信息。

> 注意：填入的用户需要具备对ApolloPortalDB和ApolloConfigDB数据的读写权限。

```
#apollo config db info
apollo_config_db_url=jdbc:mysql://localhost:3306/ApolloConfigDB?useSSL=false&characterEncoding=utf8
apollo_config_db_username=用户名
apollo_config_db_password=密码（如果没有密码，留空即可）

# apollo portal db info
apollo_portal_db_url=jdbc:mysql://localhost:3306/ApolloPortalDB?useSSL=false&characterEncoding=utf8
apollo_portal_db_username=用户名
apollo_portal_db_password=密码（如果没有密码，留空即可）
```

> 注1：由于ApolloConfigDB在每个环境都有部署，所以对不同的环境config-service和admin-service需要使用不同的数据库参数打不同的包，portal只需要打一次包即可

> 注2：如果不想config-service和admin-service每个环境打一个包的话，也可以通过运行时传入数据库连接串信息实现，具体可以参考 [Issue 869](https://github.com/ctripcorp/apollo/issues/869)

> 注3：每个环境都需要独立部署一套config-service、admin-service和ApolloConfigDB

2）配置各环境meta service地址

修改源码中apollo-portal中的apollo-env.properties文件：

![1593569570985](C:\Users\hebiao\AppData\Roaming\Typora\typora-user-images\1593569570985.png)

3）执行编译、打包

做完上述配置后，就可以执行编译和打包了。

> 注：初次编译会从Maven中央仓库下载不少依赖，如果网络情况不佳时很容易出错，建议使用国内的Maven仓库源，比如[阿里云Maven镜像](http://www.cnblogs.com/geektown/p/5705405.html)

```bash
#linux系统
./build.sh
#Windows系统 右键点击运行build.bat
```

该脚本会依次打包apollo-configservice, apollo-adminservice, apollo-portal。

> 注：由于ApolloConfigDB在每个环境都有部署，所以对不同环境的config-service和admin-service需要使用不同的数据库连接信息打不同的包，portal只需要打一次包即可

4）获取安装包上传放到服务器对应的位置

安装包位于`xxx/target/`目录下的`apollo-xxx-x.x.x-github.zip`

5）部署

将`apollo-xxx-x.x.x-github.zip`上传到服务器上，解压后执行scripts/startup.sh即可。如需停止服务，执行scripts/shutdown.sh.

> 注：服务启动顺序apollo-configservice -> apollo-adminservice -> apollo-portal