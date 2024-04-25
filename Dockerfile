FROM amazoncorretto:17-alpine-jdk

COPY target/demo-0.0.1-SNAPSHOT.war app.war

ENTRYPOINT ["java","-war","/app.war"]