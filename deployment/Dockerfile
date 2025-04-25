FROM eclipse-temurin:21-jdk-alpine

VOLUME /tmp

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

COPY applications/app-service/build/libs/*.jar NequiChallenge.jar

RUN chown appuser:appgroup NequiChallenge.jar

ENV DB_HOST=localhost \
    DB_NAME=mydb \
    DB_PASSWORD=password \
    DB_PORT=5432 \
    DB_USER=admin \
    DB_SSL_MODE=disable \
    SPRING_PROFILES_ACTIVE=prod \
    JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"

USER appuser

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar NequiChallenge.jar"]

