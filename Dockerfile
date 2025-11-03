# Etapa 1: build do projeto
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copia o pom.xml e baixa dependências
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o restante do código e compila
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: imagem final para rodar o app
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copia o .jar gerado da etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Porta padrão do Spring Boot
EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]


