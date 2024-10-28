#!/bin/bash

# Имя образа
IMAGE_NAME="java-todo"
INITIAL_VERSION="1.0.0"

# Функция для увеличения версии
increment_version() {
    local version=$1
    local major minor patch

    # Разбиваем версию на части
    IFS='.' read -r major minor patch <<< "$version"

    # Увеличиваем patch-версию на 1
    patch=$((patch + 1))

    # Если patch достиг 10, увеличиваем minor и сбрасываем patch
    if [[ $patch -ge 10 ]]; then
        patch=0
        minor=$((minor + 1))
    fi

    # Если minor достиг 10, увеличиваем major и сбрасываем minor
    if [[ $minor -ge 10 ]]; then
        minor=0
        major=$((major + 1))
    fi

    # Возвращаем новую версию
    echo "$major.$minor.$patch"
}

# Проверяем наличие образа
current_version=$(docker images --format "{{.Tag}}" "$IMAGE_NAME" | sort -V | tail -n 1)

if [[ -z "$current_version" ]]; then
    # Если образ не существует, создаем с начальной версией
    new_version=$INITIAL_VERSION
else
    # Если образ существует, увеличиваем версию
    new_version=$(increment_version "$current_version")
fi

# Имя файла .env
ENV_FILE=".env.build"

# Создаем или очищаем .env файл
echo "" > $ENV_FILE

if [ "$#" -lt 5 ] || [ "$#" -gt 5 ]; then
    echo "Использование: $0 <db_host> <db_port> <db_name> <db_user> <db_password>"
    exit 1
fi

DB_HOST=$1
DB_PORT=$2
DB_NAME=$3
DB_USER=$4
DB_PASSWORD=$5
APP_PORT=8090

# Записываем переменные в .env файл
echo "LOGGING_LEVEL=DEBUG" >> $ENV_FILE
echo "DB_URL=jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}" >> $ENV_FILE
echo "DB_USER=${DB_USER}" >> $ENV_FILE
echo "DB_PASSWORD=${DB_PASSWORD}" >> $ENV_FILE
echo "SHOW_SQL=true" >> $ENV_FILE
echo "USE_SWAGGER=true" >> $ENV_FILE
echo "HOST_URL=http://localhost:${APP_PORT}" >> $ENV_FILE
echo "SPRING_PROFILE=local" >> $ENV_FILE

# Выводим содержимое .env файла
echo ".env file created with the following content:"
cat $ENV_FILE

# Выводим новую версию
echo "Building Docker image with version: $new_version"

# Сборка Docker-образа с новой версией
docker build . --build-arg APP_VERSION=$new_version -t "$IMAGE_NAME:$new_version"

# Останавливаем старый контейнер, если существует с таким названием
docker stop java-todo || true

# Удаляем старый контейнер, если существует с таким названием
docker rm java-todo || true 

# Запуск контейнера
docker run --env-file $ENV_FILE -p $APP_PORT:8080 --restart unless-stopped --name java-todo --expose 8080 --network local -d $IMAGE_NAME:$new_version

# Удаляем .env файл после использования
rm $ENV_FILE
echo ".env file has been deleted."
