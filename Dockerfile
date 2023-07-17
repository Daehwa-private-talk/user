FROM amazoncorretto:17.0.7-al2023-headless

RUN apt-get update && apt-get install -y curl

COPY build/libs/user-0.0.1-SNAPSHOT.jar ./auth-service.jar

EXPOSE 8080

CMD ["java", "-jar", "auth-service.jar"]
