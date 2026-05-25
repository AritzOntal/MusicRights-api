FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
EXPOSE 8085
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]