FROM adoptopenjdk/maven-openjdk11 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean install -Dmaven.test.skip

FROM openjdk:11
ARG JAR_FILE=/home/app/target/accountapi-0.0.1-SNAPSHOT.jar
COPY --from=build ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]