FROM eclipse-temurin:17-jdk-alpine
COPY ./target/bot-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

# FROM alpine:3.10
# COPY entrypoint.sh /entrypoint.sh
# ENTRYPOINT ["/entrypoint.sh"]



#FROM busybox:latest
#COPY --chmod=755 <<EOF /app/run.sh
#!/bin/sh
#while true; do
#  echo -ne "The time is now $(date +%T)\\r"
#  sleep 1
#done
#EOF





#ENTRYPOINT /app/run.sh
