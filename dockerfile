FROM openjdk:8-jdk-alpine
COPY ./target/web-0.0.1-SNAPSHOT.war /usr/app/
WORKDIR /usr/app
ENTRYPOINT ["java", "-jar", "web-0.0.1-SNAPSHOT.war"]
