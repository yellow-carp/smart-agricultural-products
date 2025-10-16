#!/bin/bash

# 项目名称（或者公司名称）
PROJECT_NAME ='example-company'

# 应用 jar 全名
JAR_FULL_NAME="/home/applications/${PROJECT_NAME}/single-runner.jar"
# 应用启动端口
APP_PORT=1123
# 是否日志输出
LOGOUT='no'


# 遍历传入的所有参数
while [[ $# -gt 0 ]]; do
  arg="$1"
  case $arg in
    -port | -p)
      # 获取 -port 后面的参数作为端口号
      shift # 移动到下一个参数
      APP_PORT="$1"
      ;;
    -name | -n)
      # 获取 -name 或 -n 后面的参数作为项目名称
      shift # 移动到下一个参数
      PROJECT_NAME="$1"
      ;;
    -log | -logs)
      LOGOUT='yes'
      echo "****** 日志模式启动 ******"
      ;;
    *)
      ;;
  esac
  shift # 继续处理下一个参数
done

echo "****** 启动端口号为: ${APP_PORT} ******";

# 停止服务
pkill -f "java -jar ${JAR_FULL_NAME}";
echo "****** 服务停止,即将重新启动 ******";
sleep 1s;

if [ "${LOGOUT}" == "yes" ]; then
  # 执行服务,打印日志
  #SINGLE_SINGLE_PORT=9911 java -jar /home/applications/${PROJECT_NAME}/single-runner.jar
  SINGLE_SINGLE_PORT=${APP_PORT} java -jar ${JAR_FULL_NAME}
else
  # 执行服务（静默模式启动）
  # SINGLE_SINGLE_PORT=9911 java -jar /home/applications/${PROJECT_NAME}/single-runner.jar >/dev/null 2>&1 &
  # 若想要查看进程信息 lsof -i -P -n | grep LISTEN | grep java
  SINGLE_SINGLE_PORT=${APP_PORT} java -jar ${JAR_FULL_NAME} >/dev/null 2>&1 &

  echo "****** 静默模式启动,日志路径为 /home/applications/${PROJECT_NAME}/logs/single-runner/ ******"
  echo "****** 若想要查看进程信息：lsof -i -P -n | grep LISTEN | grep java ******"
  echo "****** 若想停止服务：pkill -f "java -jar ${JAR_FULL_NAME}"a ******"
fi
