FROM gradle:7-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle buildFatJar --no-daemon

FROM openjdk:17-jdk-slim-buster
EXPOSE 8181
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/Microservicio-Reservas-all.jar /app/microservicios-reservas.jar
ENTRYPOINT ["java","-jar","/app/microservicios-reservas.jar"]