FROM openjdk:17-oracle as builder
WORKDIR /usr/abnamro

COPY target/recipe-0.0.1-SNAPSHOT.jar recipe-0.0.1-SNAPSHOT.jar
ARG JAR_FILE=target/recipe-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} recipe-0.0.1-SNAPSHOT.jar
RUN java -Djarmode=layertools -jar recipe-0.0.1-SNAPSHOT.jar extract

FROM openjdk:17-oracle
WORKDIR /usr/abnamro

RUN groupadd abnamro && useradd -g abnamro abnamro
USER abnamro

RUN mkdir -p /var/log

COPY --from=builder /usr/abnamro/dependencies/ ./
COPY --from=builder /usr/abnamro/spring-boot-loader/ ./
COPY --from=builder /usr/abnamro/snapshot-dependencies/ ./
COPY --from=builder /usr/abnamro/application/ ./

ENTRYPOINT ["java","-noverify", "org.springframework.boot.loader.JarLauncher"]