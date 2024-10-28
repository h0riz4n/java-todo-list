# Этап 1: Сборка приложения
FROM maven:3.8.5-openjdk-17 AS build

ARG APP_VERSION=${APP_VERSION}

# Копируем файлы проекта в контейнер
COPY pom.xml .
COPY src ./src

# Запускаем сборку приложения
RUN mvn clean package -DskipTests=true -Dproject.version=${APP_VERSION}

# Этап 1: Создание образа
FROM openjdk:17

# Настраиваем местное время Европа/Москва
ENV TZ=Europe/Moscow

# Копируем скомпилированный jar-файл из предыдущего этапа
COPY --from=build ./target/*.jar todo-app.jar

# Указываем точку входа для запуска приложения
ENTRYPOINT ["java", "-jar",  "-Dspring.profiles.active=${SPRING_PROFILE}", "todo-app.jar"]