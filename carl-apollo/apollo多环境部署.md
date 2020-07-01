> 本文主要介绍apollo多环境部署需要修改的相关配置。
>
> 通过源码部署多环境。



### 一、安装包配置

​		多环境部署，各个环境都需要有单独的configservice、adminservice以及对应的db。

#### 1.1、apollo-configservice配置

##### 1.1.1 UAT环境

1）创建ApolloConfigDB_UAT数据库

导入apolloconfigdb.sql，修改数据库名

```sql
CREATE DATABASE IF NOT EXISTS ApolloConfigDB_UAT DEFAULT CHARACTER SET = utf8mb4;
...
```

2）修改scripts/build.bat

```bash
rem apollo config db info
set apollo_config_db_url="jdbc:mysql://10.101.81.131:43306/ApolloConfigDB_UAT?characterEncoding=utf8"
set apollo_config_db_username="dev_hdjf"
set apollo_config_db_password="dev123456"

rem apollo portal db info
set apollo_portal_db_url="jdbc:mysql://10.101.81.131:43306/ApolloPortalDB?characterEncoding=utf8"
set apollo_portal_db_username="dev_hdjf"
set apollo_portal_db_password="dev123456"

rem meta server url, different environments should have different meta server addresses
set dev_meta="http://172.168.1.101:8080"
set uat_meta="http://172.168.1.102:8080"
```

> 注：
>
> `dev_meta`配置dev环境的configservice的地址信息（Eureka的地址信息）
>
> `uat_meta`配置uat环境的configservice的地址信息（Eureka的地址信息）
>
> 该信息可在portal的安装包中config/apollo-env.properties中修改

3）执行编译、打包

​		执行scripts/build.bat，得到apollo-portal-1.6.1-github.zip，apollo-configservice-1.6.1-github.zip，apollo-adminservicee-1.6.1-github.zip

将安装包上传到UAT对应的服务器进行部署。



##### 1.1.2 DEV环境

1）创建ApolloConfigDB_DEV数据库

导入apolloconfigdb.sql，修改数据库名

```sql
CREATE DATABASE IF NOT EXISTS ApolloConfigDB_DEV DEFAULT CHARACTER SET = utf8mb4;
...
```

2）修改scripts/build.bat

```bash
rem apollo config db info
set apollo_config_db_url="jdbc:mysql://10.101.81.131:43306/ApolloConfigDB_DEV?characterEncoding=utf8"
set apollo_config_db_username="dev_hdjf"
set apollo_config_db_password="dev123456"

rem apollo portal db info
set apollo_portal_db_url="jdbc:mysql://10.101.81.131:43306/ApolloPortalDB?characterEncoding=utf8"
set apollo_portal_db_username="dev_hdjf"
set apollo_portal_db_password="dev123456"

rem meta server url, different environments should have different meta server addresses
set dev_meta="http://172.168.1.101:8080" 
set uat_meta="http://172.168.1.102:8080"
```

>  注：
>
> `dev_meta`配置dev环境的configservice的地址信息（Eureka的地址信息）
>
> `uat_meta`配置uat环境的configservice的地址信息（Eureka的地址信息）
>
> 该信息可在portal的安装包中config/apollo-env.properties中修改

3）执行编译、打包

​		执行scripts/build.bat，得到apollo-configservice-1.6.1-github.zip，apollo-adminservicee-1.6.1-github.zip

将安装包上传到DEV服务器进行部署。



> 注：portal只需要部署一套（测试和产线需要分别部署），configservice，adminservice各个环境都需要部署一套。