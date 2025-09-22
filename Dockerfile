###############
# BUILD STAGE #
###############
FROM maven:3 AS build

WORKDIR /build

COPY pom.xml .
COPY est-service-api ./est-service-api
COPY est-service-implementation ./est-service-implementation
COPY est-service-rest ./est-service-rest

ARG SKIP_TESTS=false
RUN --mount=type=cache,target=/root/.m2/repository \
    mvn install -DskipTests=%SKIP_TESTS%

####################
# DEPLOYMENT STAGE #
####################
FROM eclipse-temurin:21-jre-alpine AS deploy

ENV LANGUAGE="en_GB:en"

# Create less privileged user, this is a good practice to follow with docker containers
RUN addgroup -S appUserGroup && adduser -S -G appUserGroup appUser
USER appUser

COPY --chown=appUser --from=build /build/est-service-rest/target/quarkus-app/lib/ /deployments/lib/
COPY --chown=appUser --from=build /build/est-service-rest/target/quarkus-app/*.jar /deployments/
COPY --chown=appUser --from=build /build/est-service-rest/target/quarkus-app/app/ /deployments/app
COPY --chown=appUser --from=build /build/est-service-rest/target/quarkus-app/quarkus/ /deployments/quarkus

WORKDIR /deployments

CMD ["java", "-jar", "quarkus-run.jar"]