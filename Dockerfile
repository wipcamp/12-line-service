FROM maven:3.6.3-jdk-8 as builder

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn package -DskipTests -e

FROM openjdk:8-alpine3.7

WORKDIR /app

COPY --from=builder /app/target/12-user-service-application.jar /app

ENTRYPOINT ["java", "-jar", "/app/12-user-service-application.jar"]

CMD [""]
