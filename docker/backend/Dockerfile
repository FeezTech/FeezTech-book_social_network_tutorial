## BUILD STAGE
FROM maven:3.9.11-eclipse-temurin-21 AS build
WORKDIR /build

# Copy only pom.xml first for dependency caching
COPY pom.xml .
RUN mvn dependency:go-offline

# Then copy full source and package
COPY src ./src
RUN mvn clean package -DskipTests

## RUNTIME STAGE
FROM amazoncorretto:21

# Define build-time and environment variables
ARG PROFILE=dev
ARG APP_VERSION=1.0.3

ENV ACTIVE_PROFILE=${PROFILE}
ENV JAR_VERSION=${APP_VERSION}
ENV DB_URL=jdbc:postgresql://postgres-sql-bsn:5432/book_social_network

WORKDIR /app

# Copy the generated JAR file from the build stage
COPY --from=build /build/target/book-network-${APP_VERSION}.jar /app/app.jar

EXPOSE 8088

# Run the application
CMD ["sh", "-c", "java -Dspring.profiles.active=${ACTIVE_PROFILE} -Dspring.datasource.url=${DB_URL} -jar app.jar"]
