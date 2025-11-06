# Etapa 1: Build com Maven
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copia o POM primeiro e baixa dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o código e compila
COPY . .
RUN mvn clean package -DskipTests -Dmaven.test.skip=true -T 1C

# Etapa 2: Runtime leve e estável
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copia o JAR da etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta padrão
EXPOSE 8080

# Comando de inicialização
ENTRYPOINT ["java", "-jar", "app.jar"]




	

