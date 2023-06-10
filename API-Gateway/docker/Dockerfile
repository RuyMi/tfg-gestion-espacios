FROM gradle:7-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

RUN gradle buildFatJar --no-daemon
FROM openjdk:17-jdk-slim-buster
EXPOSE 8080
RUN mkdir /app
RUN mkdir /cert
COPY --from=build /home/gradle/src/cert/* /cert/
COPY --from=build /home/gradle/src/build/libs/API-Gateway-all.jar /app/microservicios-gateway.jar
ENTRYPOINT ["java","-jar","/app/microservicios-gateway.jar"]