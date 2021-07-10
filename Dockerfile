FROM openjdk:11-jre-slim@sha256:31a5d3fa2942eea891cf954f7d07359e09cf1b1f3d35fb32fedebb1e3399fc9e
RUN mkdir /app
COPY ./target/*.jar /app/java-application.jar
WORKDIR /app
CMD ["java", "-jar", "java-application.jar"]