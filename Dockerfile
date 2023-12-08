FROM eclipse-temurin:21-jdk-alpine
RUN mkdir -p /data/heap-dumps
COPY ./build/libs/*.jar app.jar
COPY ./build/resources/main /resources
CMD ["java", \
    "-XX:+HeapDumpOnOutOfMemoryError", \
    "-XX:HeapDumpPath=/data/heap-dumps", \
    "-Djava.security.egd=file:/dev/urandom", \
    "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", \
    "-jar", \
    "/app.jar"]
EXPOSE 5005 8080
