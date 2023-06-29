FROM openjdk:11
EXPOSE 8080
ADD target/spring-boot-elastic-search-0.0.1-SNAPSHOT.jar spring-boot-elastic-search-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/spring-boot-elastic-search-0.0.1-SNAPSHOT.jar"]
