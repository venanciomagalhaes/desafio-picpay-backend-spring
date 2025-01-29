# Etapa 1: Construção do JAR com Maven
FROM maven:3.9.7-eclipse-temurin-21 AS builder

WORKDIR /app

# Copia os arquivos para dentro do container
COPY . .

# Compila o projeto e gera o JAR
RUN mvn clean package -DskipTests

# Etapa 2: Imagem final com OpenJDK 21
FROM openjdk:21-jdk

WORKDIR /app

# Copia apenas o JAR gerado na etapa anterior
COPY --from=builder /app/target/*.jar /app/app.jar

# Copia o application.yml para dentro do container
COPY src/main/resources/application.yml /app/application.yml

EXPOSE 8080

# Executa a aplicação
CMD ["java", "-jar", "/app/app.jar"]
