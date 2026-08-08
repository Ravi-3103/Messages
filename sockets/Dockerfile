FROM eclipse-temurin:17-jre
WORKDIR /app
COPY sockets/target/sockets-app.jar app.jar
COPY sockets/config/ config/
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
