FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/config-server-*.jar app.jar
ENV CONFIG_REPO_PATH=/config-repo
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]
