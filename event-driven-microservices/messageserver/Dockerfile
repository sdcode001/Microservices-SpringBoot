FROM openjdk:26-ea-jdk-slim

MAINTAINER com.sd

RUN apt-get update && \
    apt-get install -y curl && \
    rm -rf /var/lib/apt/lists/*

COPY target/messageserver-0.0.1-SNAPSHOT.jar messageserver-0.0.1-SNAPSHOT.jar

EXPOSE 9010

ENTRYPOINT ["java", "-jar", "messageserver-0.0.1-SNAPSHOT.jar"]