FROM public.ecr.aws/docker/library/gradle:8.4.0-jdk21 AS build
WORKDIR /app
COPY . .
RUN ./gradlew clean build -x :domain:generateAvroJava --no-daemon

#FROM public.ecr.aws/amazoncorretto/amazoncorretto:21.0.5-al2023-headless
FROM gcr.io/distroless/java21-debian12:latest
COPY --from=build --chmod=755 /app/interface/build/libs/inventory.jar /inventory.jar
COPY --chmod=755 docker-entrypoint.sh /docker-entrypoint.sh

ENV LC_ALL=C.UTF-8

ENTRYPOINT ["java", "-jar", "/inventory.jar"]
