# Etapa 1: Build com Maven
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copia o POM e baixa dependências primeiro
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o restante do código e compila
COPY . .
RUN mvn clean package -DskipTests -Dmaven.test.skip=true -T 1C

# Etapa 2: Runtime Java
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copia o JAR da etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta padrão
EXPOSE 8080

# Executa o app
ENTRYPOINT ["java", "-jar", "app.jar"]




	

