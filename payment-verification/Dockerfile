FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY payment-verification/.mvn/ .mvn
COPY payment-verification/mvnw payment-verification/pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline
COPY payment-verification/src ./src
RUN ./mvnw clean package -DskipTests
EXPOSE 8080
CMD ["java", "-jar", "target/payment-verification-1.0.0.jar"]