FROM openjdk:23-jdk

RUN microdnf install -y maven
WORKDIR /app
COPY . .

RUN mvn install 
RUN mvn clean package 

ENTRYPOINT ["java", "-jar", "./target/closedtrivia-0.0.1-SNAPSHOT.jar"]

