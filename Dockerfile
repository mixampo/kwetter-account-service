# Build stage #
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

# Package stage #
FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/kwetter-account-service-1.0-SNAPSHOT.war /usr/local/lib/kwetter-account-service-1.0-SNAPSHOT.war
EXPOSE 80
ENTRYPOINT ["java","-jar","/usr/local/lib/kwetter-account-service-1.0-SNAPSHOT.war"]
