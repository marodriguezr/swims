# Software Impact Measurement System

## How to run

Having **Maven**, **Java** and **Docker** installed locally.

### Development environment

1. Clone the repository.
2. Run:
   ```bash
   docker compose -f docker-compose.yml -f docker-compose.dev.yml up -d
   ```
3. Compile the code of the database schema available at `db-schema.dbml` on [dbdiagram](https://www.dbdiagram.io.d).
4. Execute the compiled sql script on the database, you can use tools such as dbeaver.
5. Package and deploy the aplication running:
   ```bash
   mvn clean package && docker cp ./swimsEAP/target/swims-1.0.ear swims_wildfly_dev:/app
   ```
   or compiling within a docker container
   ```bash
   docker run -it --rm --name swims_mvn_dev -v "$(pwd)":/usr/src/mymaven -v ~/.m2:/root/.m2 -w /usr/src/mymaven maven:3-openjdk-11 mvn clean install
   ```

## Command cheatsheet

1. Deploy application to docker container

   ```bash
   mvn clean package && docker cp ./swimsEAP/target/swims-1.0.ear swims_wildfly_dev:/app
   ```
