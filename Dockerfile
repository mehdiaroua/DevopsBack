FROM openjdk:8-alpine
COPY target/DevOps_Project-1.0.jar /app/app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]