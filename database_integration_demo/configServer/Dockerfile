FROM openjdk:26-ea-jdk-slim

MAINTAINER com.sd

RUN apt-get update && \
    apt-get install -y curl && \
    rm -rf /var/lib/apt/lists/*

COPY target/configServer-0.0.1-SNAPSHOT.jar configServer-0.0.1-SNAPSHOT.jar

EXPOSE 8071

ENTRYPOINT ["java", "-jar", "configServer-0.0.1-SNAPSHOT.jar"]