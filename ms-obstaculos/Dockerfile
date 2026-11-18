# Etapa 1: build com Maven e JDK 25
FROM maven:3.9.16-eclipse-temurin-25 AS build
WORKDIR /opt/app
COPY . .
# os testes ficam fora da imagem (rodam com ./mvnw package)
RUN mvn clean package -DskipTests

# Etapa 2: imagem final apenas com o JRE 25 e o JAR
FROM eclipse-temurin:25-jre
LABEL application="ms-obstaculos"
WORKDIR /opt/app
COPY --from=build /opt/app/target/*.jar /opt/app/app.jar
# sem EXPOSE: a porta é aleatória (server.port=0) e o endereço é registrado no Eureka
ENTRYPOINT ["java", "-jar", "app.jar"]
