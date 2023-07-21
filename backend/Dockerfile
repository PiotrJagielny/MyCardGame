FROM maven:3.8.7-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim

COPY --from=build /targer/demo-0.0.1-SNAPSHOT.jar demo.jar
EXPOSE 8000
ENTRYPOINT ["java", "-jar", "demo.jar"]