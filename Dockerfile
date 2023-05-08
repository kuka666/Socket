FROM adoptopenjdk/openjdk11:alpine-jre

RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

WORKDIR /app

COPY target/socket-0.1.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]