# Build phase with maven
FROM maven:3-openjdk-11 as build
## Setting working directory
WORKDIR /usr/src/mymaven
## Copying project
COPY ./swimsEAP /usr/src/mymaven/swimsEAP
COPY ./swimsEJB /usr/src/mymaven/swimsEJB
COPY ./swimsWeb /usr/src/mymaven/swimsWeb
COPY ./pom.xml /usr/src/mymaven/pom.xml

## Cleaning, installing dependencies and packaging
RUN mvn clean install && mvn clean package

# Start phase
FROM docker.io/bitnami/wildfly:26
## Copying postgresql-42.5.0.jar file
COPY ./docker/prod/wildfly/postgresql-42.5.0.jar /app
## Copying project from build phase
COPY --from=build /usr/src/mymaven/swimsEAP/target/swims-1.0.ear /app
## Copying standalone.xml configuration file
COPY ./docker/prod/wildfly/standalone.xml /bitnami/wildfly/configuration/standalone.xml