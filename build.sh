#!/bin/bash

if [ "$#" -lt 6 ] || [ "$#" -gt 6 ]; then
    echo "Использование: $0 <версия> <DB_HOST> <DB_PORT> <DB_NAME> <DB_USER> <DB_PASSWORD>"
    exit 1
fi

APP_VERSION=$1
DB_HOST=$2
DB_PORT=$3
DB_NAME=$4
DB_USER=$5
DB_PASSWORD=$6
APP_PORT=8090

docker build . --build-arg APP_VERSION=${APP_VERSION} -t java-todo:${APP_VERSION} 
docker stop java-todo || true
docker rm java-todo || true 
docker run -p ${APP_PORT}:8080 \
--network local \
--expose 8080 \
--name java-todo \
-e LOGGING_LEVEL=DEBUG \
-e DB_URL=jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME} \
-e DB_PASSWORD=${DB_PASSWORD} \
-e DB_USER=${DB_USER} \
-e SHOW_SQL=true \
-e USE_SWAGGER=true \
-e HOST_URL=http://localhost:${APP_PORT} \
-d java-todo:$APP_VERSION
