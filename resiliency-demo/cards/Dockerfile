FROM openjdk:26-ea-jdk-slim

MAINTAINER com.sd

COPY target/cards-0.0.1-SNAPSHOT.jar cards-0.0.1-SNAPSHOT.jar

EXPOSE 9000

ENTRYPOINT ["java", "-jar", "cards-0.0.1-SNAPSHOT.jar"]