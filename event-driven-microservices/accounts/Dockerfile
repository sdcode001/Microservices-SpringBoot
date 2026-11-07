FROM openjdk:26-ea-jdk-slim

MAINTAINER com.sd

RUN apt-get update && \
    apt-get install -y curl && \
    rm -rf /var/lib/apt/lists/*

COPY target/accounts-0.0.1-SNAPSHOT.jar accounts-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "accounts-0.0.1-SNAPSHOT.jar"]

