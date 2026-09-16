FROM openjdk:26-ea-jdk-slim

MAINTAINER com.sd

COPY target/loans-0.0.1-SNAPSHOT.jar loans-0.0.1-SNAPSHOT.jar

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "loans-0.0.1-SNAPSHOT.jar"]