## FOR BUILDING STAGE
FROM maven:3.9.11-eclipse-temurin-21 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

## RUNTIME STAGE
FROM amazoncorretto:21

# Declare build-time arguments (passed from docker-compose)
ARG PROFILE
ARG APP_VERSION

##DEFINE FEW THINGS

WORKDIR /app
COPY --from=build /build/target/book-network-*.jar /app/
EXPOSE 8088

# Declare environment variables (these will come from docker-compose)
ENV DB_URL=""
ENV ACTIVE_PROFILE=${PROFILE}
ENV JAR_VERSION=${APP_VERSION}
ENV EMAIL_HOSTNAME=""
ENV EMAIL_USERNAME=""
ENV EMAIL_PASSWORD=""


CMD sh -c "java -jar -Dspring.profiles.active=${ACTIVE_PROFILE} -Dspring.datasource.url=${DB_URL} book-network-${JAR_VERSION}.jar"


