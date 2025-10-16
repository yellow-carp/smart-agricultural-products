##### 部署配置

* Nginx 配置 [nginx-api.conf](nginx-api.conf)
  注意示例中仅仅测试使用,没有正式,仅仅监听了 80 端口,生产环境请补充证书和 443 端口监听

* 部署脚本 [start.sh](start.sh)

启动脚本默认不传递任何参数,默认端口号为 1123,默认为后台静默模式启动;

> 参数支持 :
>* 端口支持 : -p 端口号 , 或者 -port 端口号,默认值 1123,如 -p 8080 或者 -port 8080将以您配置的端口号进行启动
>* 控制台日志 : -log , 或者 -logs
>* 项目名称: -name , 或者 -n


示例: ./start.sh -p 8080 -log -name my-project ; 参数不区分顺序

* 停机脚本 [stop.sh](stop.sh)
> 参数支持
>* 项目名称: -name , 或者 -n

示例：./stop.sh -name my-project