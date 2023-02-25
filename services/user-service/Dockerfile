FROM openjdk:17-jdk-slim
LABEL org.opencontainers.image.source https://github.com/tshen-dev/my-pet
EXPOSE 8080
ARG version=unkown
ARG service_name=unkown
RUN mkdir /app
COPY build/libs/$service_name-$version.jar /app/spring-boot-application.jar
ENTRYPOINT ["java", "-jar", "/app/spring-boot-application.jar"]