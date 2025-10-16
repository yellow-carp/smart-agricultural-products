#!/bin/bash

# 项目名称（或者公司名称）
PROJECT_NAME ='example-company'

# 应用 jar 全名
JAR_FULL_NAME="/home/applications/${PROJECT_NAME}/single-runner.jar"

# 停止服务
pkill -f "java -jar ${JAR_FULL_NAME}"