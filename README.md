##### 启动准备

* hosts 配置
  >* 启动该项目需要配置hosts 文件,推荐下载 [hosts管理工具](https://github.com/oldj/SwitchHosts)
  >* hosts内容如下 [hosts](./hosts);注意需要修改ip为实际ip

   ```shell
     # Redis 地址(仅仅后端启动配置)
     192.168.1.3 xtechcn-single-redis
     # MySQL 链接地址(仅仅后端启动配置)
     192.168.1.248 xtechcn-single-mysql
  
     # 服务请求地址(前端配置,非必须)
     192.168.1.250 dev.single.xtechcn.com
   ```

* 数据库脚本初始化
  >* 自行创建数据库,然后在数据库以下初始化数据脚本
  >* RBAC以及系统必须配置在归为 UPMS 模块，初始化的数据脚本： [system_init.sql](./database/system/system_init.sql)
  >* OSS 文件以及其他系统基础配置管理为 Base 模块,数据初始化的脚本：[base_init.sql](./database/system/base_init.sql)
  >* 定时任务调度使用 Quart，模块为 Job 模块,数据初始化的脚本：[qrtz_init_v2.3.0.sql](./database/job/qrtz_init_v2.3.0.sql)
  >* 业务数据脚本和数据模型存放到[business](./database/business) 下,可根据需求自行添加和修改(如代码生成器示例就在其中)

* 数据库配置
  >* 数据库连接需要配置 hosts xtechcn-single-mysql 或者设置环境变量 MYSQL_HOST,注意请修改为自己的数据库用户名密码
  >* 数据库配置在 [application-jdbc-ds.yml](xtechcn-cloud/single-business/single-runner/src/main/resources/application-jdbc-ds.yml)datasource项配置
  >* MySQL 中如果没有使用Root 用户,需要在数据库中创建用户,并授权,创建用户 和授权数据库访问的命令如下：

    ```mysql
    -- 创建用户名为'user'是不限制用户访问的IP也就是'%',密码为'password'
    CREATE USER 'user'@'%' IDENTIFIED BY 'password';
  
    -- 授权 user可以操作db_test库的指定的权限
    GRANT
    SELECT,
    INSERT,
    UPDATE,
    DELETE,
    CREATE,
    REFERENCES,
    INDEX,
    ALTER
    ON
    `datebase_name`.*
    TO
    `user_name`@'%';
    ```

* Redis 配置
  >* Redis 连接需要配置 hosts xtechcn-single-redis 或者设置环境变量 REDIS_HOST
  >* Redis 以及 RedisCache 配置在 [application-redis.yml](xtechcn-cloud/single-business/single-runner/src/main/resources/application-redis.yml)
  >* Redis 项目密码可以直接修改配置文件,也可以通过配置 REDIS_PASSWORD 完成密码赋值

* 认证配置
  >* 认证框架使用 SpringSecurity
  >*认证框架,需要配置认证信息,配置在[application-auth.yml](xtechcn-cloud/single-business/single-auth/src/main/resources/application-auth.yml)
  >* Token 支持Jwt 客户端Token 支持,以及服务端Token,Redis 支持，通过配置文件可动态支持配置

##### 部署配置

* Nginx 配置 [nginx-api.conf](../doc/nginx-api.conf)
  >* 注意示例中仅仅测试使用,没有正式,仅仅监听了 80 端口,生产环境请补充证书和 443 端口监听

* 部署脚本 [start.sh](./doc/start.sh)

  > 启动脚本默认不传递任何参数,默认端口号为 1123,默认为后台静默模式启动;

* 参数支持 :
  >* 端口支持 : -p 端口号 , 或者 -port 端口号,默认值 1123,如 -p 8080 或者 -port 8080将以您配置的端口号进行启动
  >* 控制台日志 : -log , 或者 -logs , 或者 log , 或者 logs
  >* 示例: ./start.sh -p 8080 -log ; 或者 ./start.sh -logs -port 8080

* 停机脚本 [stop.sh](./doc/stop.sh)

##### docker-compose 模式启动

* 打包java 服务 获得 Jar 文件

  ```shell
    mvn clean package -Dmaven.test.skip=true
  ```

* 获得 Jar 文件, 将文件copy 到 docker-compose 启动目录下
  ```shell
    cp single-runner.jar /xxx/xxx/docker/single-runner.jar
  ```

##### 启动服务

  ```shell
    # 无后台数据层启动(静默模式)
    docker-compose up -d
  
    # 有后台数据层启动(非静默模式)
    docker-compose up
  ```

##### 查看日志

  ```shell
    # 查看服务 xtechcn-single-fresh 500 行日志,可以查看多个服务日志
    docker-compose logs -tf --tail 500 xtechcn-single-fresh
  ```

##### 关于配置信息环境变量配置方式

配置文件中的配置信息可以直接修改配置文件,也可以通过环境变量的方式配置,环境变量配置方式如下:

* MySQL 环境变量支持

|     环境变量名称     |      示例值       | 描述                 |
|:--------------:|:--------------:|:-------------------|
|   MYSQL_HOST   | 192.168.1.100  | MySQL连接地址          |
|   MYSQL_PORT   |      3306      | MySQL连接端口,默认值 3306 |
| MYSQL_DATABASE | xtechcn_single | MySQL连接数据库名称       |
|   MYSQL_USER   |      root      | MySQL连接用户名         |
| MYSQL_PASSWORD |     123456     | MySQL连接密码          |

* IDEA 中配置

```angular2html
MYSQL_HOST=192.168.1.100;MYSQL_PORT=3306;MYSQL_DATABASE=xtechcn_single;MYSQL_USER=root;MYSQL_PASSWORD=123456
```

* evn 文件配置如下：

```angular2html
MYSQL_HOST=192.168.1.100
MYSQL_PORT=3306
MYSQL_DATABASE=xtechcn_single
MYSQL_USER=root
MYSQL_PASSWORD=123456
```

* Redis 环境变量支持

|        环境变量名称         |      示例值      | 描述                                                  |
|:---------------------:|:-------------:|:----------------------------------------------------|
|      REDIS_HOST       | 192.168.1.100 | Redis连接地址                                           |
|      REDIS_PORT       |     6379      | Redis连接端口，默认值 6379                                  |
|    REDIS_DATABASE     |       0       | Redis库索引，默认值0                                       |
|    REDIS_PASSWORD     |    123456     | Redis连接密码                                           |
| REDIS_CONNECT_TIMEOUT |      10s      | Redis连接超时时间,默认值 10s                                 |
|     REDIS_TIMEOUT     |      10s      | Redis连接读超时时间,默认值 10s                                |
|   REDIS_CLIENT_TYPE   |    lettuce    | RedisConnectionFactory类型（Jedis / Lettuce 默认Lettuce） |

* IDEA 中配置

```angular2html
REDIS_HOST=192.168.1.100;REDIS_PORT=6379;REDIS_DATABASE=0;REDIS_PASSWORD=123456;REDIS_CONNECT_TIMEOUT=10s;REDIS_TIMEOUT=10s;REDIS_CLIENT_TYPE=lettuce
```

* evn 文件配置如下：

```angular2html
REDIS_HOST=192.168.1.100
REDIS_PORT=6379
REDIS_DATABASE=0
REDIS_PASSWORD=123456
REDIS_CONNECT_TIMEOUT=10s
REDIS_TIMEOUT=10s
REDIS_CLIENT_TYPE=lettuce
```

* 缓存环境变量支持

|        环境变量名称        |      示例值       | 描述                            |
|:--------------------:|:--------------:|:------------------------------|
|      CACHE_TYPE      |     redis      | 缓存类型，默认 redis                 |
|  CACHE_NULL_VALUES   |      true      | 防止缓存穿透问题,允许存储 null 值,默认值 true |
|   CACHE_KEY_PREFIX   | xtechcn:cache: | 缓存key前缀,默认值 xtechcn:cache:    |
| CACHE_USE_KEY_PREFIX |      true      | 是否使用缓存key前缀,默认值 true          |
|  CACHE_TIME_TO_LIVE  |     57600s     | 缓存有效期,本系统默认值 57600s           |

* IDEA 中配置

```angular2html
CACHE_TYPE=redis;CACHE_NULL_VALUES=true;CACHE_KEY_PREFIX=xtechcn:cache:;CACHE_USE_KEY_PREFIX=true;CACHE_TIME_TO_LIVE=57600s
```

* evn 文件配置如下：

```angular2html
CACHE_TYPE=redis
CACHE_NULL_VALUES=true
CACHE_KEY_PREFIX=xtechcn:cache:
CACHE_USE_KEY_PREFIX=true
CACHE_TIME_TO_LIVE=57600s
```

* 其他环境变量支持

|      环境变量名称       |                示例值                | 描述                                             |
|:-----------------:|:---------------------------------:|:-----------------------------------------------|
| ENABLE_SWAGGER_UI |               true                | 是否启用Swagger-UI，默认false                         |
|    TOKEN_TYPE     |             token 类型              | Token类型，支持 jwt 和 redis 服务端token，默认jwt客户端token  |
|  AUTH_TOKEN_URL   | http://www.xtechcn.com/auth/login | 登录地址认证地址，默认值 http://www.xtechcn.com/auth/login |                                      |

* IDEA 中配置

```angular2html
ENABLE_SWAGGER_UI=true;TOKEN_TYPE=redis;AUTH_TOKEN_URL=http://127.0.0.1:1123/auth/login
```

* evn 文件配置如下：

```angular2html
ENABLE_SWAGGER_UI=true
TOKEN_TYPE=jwt
```

##### 前端管理后台启动
>* cd xtechcn-vue
>* 执行命令 npm i 安装依赖包
>* 启动项目 npm run dev
>* 访问地址 http://localhost:8888 ; 用户名：admin 密码：Asia8888# SmartAgriculturalProducts
