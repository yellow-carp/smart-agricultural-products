#!/bin/bash

app_dir=/home/git/applications

# 部署更新的服务名称
app_name=xtechcn-single-fresh

# 部署环境
env=$DEPLOY_ENV

# 新增：标记是否为slave模式
is_slave=false

echo "*************************** 部署环境为 $env ***************************"

echo ">>>>>>>>>>>>>>> 您输入的参数 $* <<<<<<<<<<<<<<<"
sleep 1s

cd $app_dir

# 参数处理
for arg in "$@"; do
	# 参数中剔除用户名和密码
	if [ "${arg}" == "slave" ]; then
		# 启动的是从服务
  		app_name="${app_name}-slave"
  		is_slave=true
  		continue
  	fi
done

# 只有在非slave模式下才构建镜像
if [ "$is_slave" = false ]; then
	# 构建镜像
	docker-compose build $app_name
	echo "*************************** 构建新镜像完成 ***************************"
fi

# 测试环境修改环境文件
if [ "$env" == "dev" ]; then
  sed -i 's/DEPLOY_ENV=prod/DEPLOY_ENV=dev/g' ${app_dir}/.env
fi

# 创建网络（如果已经存在则无需创建）
docker network create -d bridge single-app-network

echo "*************************** 即将更新服务 $app_name ***************************"

docker-compose stop "${app_name}"

docker-compose up -d "${app_name}"
sleep 5s

# 启动日志控制台输出
docker-compose logs -tf --tail 1000 ${app_name}
