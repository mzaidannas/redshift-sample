FROM gradle:7.4.2-alpine as build
WORKDIR /redshift-sample
COPY build.gradle build.gradle
COPY settings.gradle settings.gradle
RUN gradle --write-verification-metadata sha256 help
COPY conf conf
COPY src src
RUN gradle shadowJar

FROM ghcr.io/graalvm/graalvm-ce:latest as package
WORKDIR /redshift-sample
COPY --from=build /redshift-sample/build/libs/redshift-sample-1.0.0-all.jar app.jar
COPY conf conf
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
