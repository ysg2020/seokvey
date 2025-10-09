FROM eclipse-temurin:17-jre AS runtime

ENV TZ=Asia/Seoul \
    LANG=ko_KR.UTF-8 \
    LANGUAGE=ko_KR:ko \
    LC_ALL=ko_KR.UTF-8
RUN useradd -ms /bin/bash ysg
USER ysg

WORKDIR /app
COPY build/libs/*-SNAPSHOT.jar app.jar

ENV JAVA_OPTS=""

EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s --retries=3 CMD curl -fsS http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh","-c","java -Dfile.encoding=UTF-8 -XX:MaxRAMPercentage=75.0 $JAVA_OPTS -jar /app/app.jar"]