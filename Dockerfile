# Etapa 1: build da aplicação
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# copia pom.xml primeiro para aproveitar cache das dependênciaDockerfiles
COPY pom.xml .
RUN mvn dependency:go-offline -q

# copia código e faz o build
COPY src ./src
RUN mvn clean package -DskipTests -q

# Etapa 2: imagem final
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]