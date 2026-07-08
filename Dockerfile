FROM eclipse-temurin:17
WORKDIR /app
COPY target/AuthService-0.0.1-SNAPSHOT.jar /app/AuthService.jar
ENTRYPOINT ["java","-jar","/app/AuthService.jar"]
EXPOSE 8083
