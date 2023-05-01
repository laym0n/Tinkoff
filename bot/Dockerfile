FROM eclipse-temurin:17-jdk-alpine
#RUN ls /tmp
COPY ./target/scrapper-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
# Container image that runs your code
# FROM alpine:3.10

# Copies your code file from your action repository to the filesystem path `/` of the container
# COPY entrypoint.sh /entrypoint.sh

# Code file to execute when the docker container starts up (`entrypoint.sh`)

# ENTRYPOINT ["/entrypoint.sh"]
