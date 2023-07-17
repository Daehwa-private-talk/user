FROM amazoncorretto:17.0.7-al2023-headless

COPY build/libs/user-0.0.1-SNAPSHOT.jar ./auth-service.jar
COPY .env ./env

EXPOSE 8080

CMD ["java", "-jar", "auth-service.jar", "--spring.config.name=.env"]
