# Assignment for ABN AMRO Company
This application was generated  by Alireza Zabetan
[alireza.zabetan@gmail.com](alireza.zabetan@gmail.com).
## Development

## Technologies

- Spring webmvc/jpa data/hibernate
- **[Maven](https://maven.apache.org/)**
- Java 17
- Postgresql
- Docker
- Swagger/OpenApi/Springfox
- Mockito

## Getting Started

### Requirements

- Docker
   - Needed in case you want to run it with Docker.
- Java 17
   - Need have the JAVA_HOME set in case you want to execute via Maven

### Building

At the project root folder, execute:

```shell
maven clean package
```

### Running

- Run the docker compose file

````bash
      docker-compose -f "docker-compose.yml" up --build -d
````

- Run without docker

* You need to have postgresql installed

```
java -jar target/recipe-0.0.1-SNAPSHOT.jar
```

If you want to run the project in local need to install postgresDB and set the below params with your values
## Postgresql configuration

The database settings in `src/main/resources/application.yml` are configured for this image.
You can put your config instead them.

```properties
spring:
  datasource:
    username: recipe
    password: recipe
```

And then can run the below command for run the app 

    mvn spring-boot:run

### swagger

When the project was running you can see all the api in swagger ui in url  
[http://localhost:9091/swagger-ui/index.html#/](http://localhost:8081/swagger-ui/index.html#/).
