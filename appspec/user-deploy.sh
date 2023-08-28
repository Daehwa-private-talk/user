#!/bin/bash

REPOSITORY=/home/ec2-user/user/build
PROJECT_NAME=user

echo "> Build 파일 복사"

sudo kill $(sudo lsof -t -i:8080)

echo "> 새애플리케이션 배포"

JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR Name:$JAR_NAME"

echo "> $JAR_NAME 에 실행권한 추가"

chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"

sudo nohup java -jar $JAR_NAME --spring.config.location=/home/ec2-user/config/application.yml > /home/ec2-user/springlogs/spring.log 2>&1 &

tail -f springlogs/spring.log

echo "> 새 애플리케이션 배포 완료"
