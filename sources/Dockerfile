FROM openjdk:17-ea-11-jdk-slim

WORKDIR /server

COPY ./build/libs/vground.jar vground.jar

ENTRYPOINT ["java", "-jar", "vground.jar"]
