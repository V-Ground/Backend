#!/bin/bash

echo "starting server ..."

# 서버 상태 확인
# 기존에 서버가 존재한다면 서버 kill

# 기존에 서버가 존재하지 않을 때, build -> java -jar

function get_server_status() {
  CURRUNT_PID=$(pgrep -f vground*.jar)
  echo "현재 구동중인 애플리케이션 pid : $CURRENT_PID"
}