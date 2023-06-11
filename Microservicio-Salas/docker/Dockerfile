FROM gradle:7-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

RUN gradle buildFatJar --no-daemon
FROM openjdk:17-jdk-slim-buster
EXPOSE 8282
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/Microservicio-Salas-all.jar /app/microservicios-salas.jar
ENTRYPOINT ["java","-jar","/app/microservicios-salas.jar"]