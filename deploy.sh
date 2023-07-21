#!/bin/bash

# 현재 실행 중인 도커 컨테이너 중에서 redis 라는 이름을 가진 컨테이너가 있는지 확인합니다.
# 만약 없다면 redis를 실행시킵니다.
if [ $(docker ps | grep -c "redis") -eq 0 ]; then
  echo "### Starting redis ###"
  docker-compose up -d redis
else
  echo "redis is already running"
fi

APP_NAME=web

IS_GREEN=$(docker ps | grep green) # 현재 실행중인 App이 blue인지 확인
DEFAULT_CONF=" /etc/nginx/nginx.conf"

if [ -z $IS_GREEN  ];then # blue라면 (IS_GREEN이 비어 있다면)

  echo "### BLUE => GREEN ###"

  echo "1. green container up"
  docker-compose up -d green # 컨테이너 실행

  while [ 1 = 1 ]; do # health check (무한 반복문을 수행합니다. 3초마다 반복)
  echo "2. green health check..."
  sleep 3

  REQUEST=$(curl 0.0.0.0:8080) # green으로 request
    if [ -n "$REQUEST" ]; then # 서비스 가능하면 health check 중지
            echo "health check success"
            break ;
            fi
  done; # while 문 마지막

  echo "3. reload nginx"
  sudo cp /etc/nginx/nginx.green.conf /etc/nginx/nginx.conf # nginx.conf 복사
  sudo nginx -s reload

  echo "4. blue container down"
  docker-compose stop blue # blue 컨테이너를 중지합니다
else # Green라면
  echo "### GREEN => BLUE ###"

  echo "1. blue container up"
  docker-compose up -d blue

  while [ 1 = 1 ]; do
    echo "2. blue health check..."
    sleep 3
    REQUEST=$(curl 0.0.0.0:8081) # blue로 request

    if [ -n "$REQUEST" ]; then # 서비스 가능하면 health check 중지
      echo "health check success"
      break ;
    fi
  done;

  echo "3. reload nginx"
  sudo cp /etc/nginx/nginx.blue.conf /etc/nginx/nginx.conf
  sudo nginx -s reload

  echo "4. green container down"
  docker-compose stop green
fi