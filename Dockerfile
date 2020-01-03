FROM maven:3.6.3-jdk-8 as builder

WORKDIR /app

COPY pom.xml .
COPY line-login.iml .
COPY linebot.login.iml .

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn package -DskipTests -e

FROM openjdk:8-alpine3.7

WORKDIR /app

COPY --from=builder /app/target/line-login.jar /app

ENTRYPOINT ["java", "-jar", "/app/line-login.jar"]

CMD [""]
