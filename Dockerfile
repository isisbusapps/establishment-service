###############
# BUILD STAGE #
###############
FROM maven:3-eclipse-temurin-21 AS build

WORKDIR /build

COPY pom.xml .
COPY est-service-api/pom.xml ./est-service-api/pom.xml
COPY est-service-implementation/pom.xml ./est-service-implementation/pom.xml
COPY est-service-rest/pom.xml ./est-service-rest/pom.xml
RUN --mount=type=cache,target=/root/.m2/repository \
    mvn dependency:go-offline -B --no-transfer-progress

COPY est-service-api ./est-service-api
COPY est-service-implementation ./est-service-implementation
COPY est-service-rest ./est-service-rest

ARG SKIP_TESTS=false
RUN --mount=type=cache,target=/root/.m2/repository \
    mvn install -DskipTests=${SKIP_TESTS} -B --no-transfer-progress

####################
# DEPLOYMENT STAGE #
####################
FROM eclipse-temurin:21-jre-alpine AS deploy

ENV LANGUAGE="en_GB:en"

# Create less privileged user, this is a good practice to follow with docker containers
RUN addgroup -S appUserGroup && adduser -S -G appUserGroup appUser
USER appUser

COPY --chown=appUser --from=build /build/est-service-rest/target/quarkus-app/ /deployments/

WORKDIR /deployments

CMD ["java", "-jar", "quarkus-run.jar"]