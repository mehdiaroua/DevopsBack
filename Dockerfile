FROM openjdk:8-alpine
ADD target/DevOps_Project-1.0.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]