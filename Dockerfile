FROM openjdk:11
EXPOSE 8080
ADD target/elastic-search-0.0.1-SNAPSHOT.jar elastic-search-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/elastic-search-0.0.1-SNAPSHOT.jar"]
