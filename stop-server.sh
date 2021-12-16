#!/bin/bash

echo "------- restart.sh triggered ... -------"

# 서버 상태 확인
# 기존에 서버가 존재한다면 서버 kill

# 기존에 서버가 존재하지 않을 때, build -> java -jar
function get_server_status() {
  CURRENT_PID=$(pgrep -f vground*.jar)
}

get_server_status

if [ -z "$CURRENT_PID" ]; then
  echo "server not found"
else
  echo "server is running ..."
  kill -15 $CURRENT_PID
fi
