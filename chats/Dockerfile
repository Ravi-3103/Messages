FROM eclipse-temurin:17-jre
WORKDIR /app
COPY chats/target/chats-app.jar app.jar
COPY chats/config/ config/
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
