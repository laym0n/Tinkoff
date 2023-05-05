FROM eclipse-temurin:17-jdk-alpine
ARG BOT_TOKEN
ENV BOT_TOKEN=${BOT_TOKEN}
COPY ./target/bot-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]


