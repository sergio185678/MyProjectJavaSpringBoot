FROM amazoncorretto:17-alpine-jdk

COPY target/demo-0.0.1-SNAPSHOT.war app.war

COPY target/classes/uploads /uploads

ENTRYPOINT ["java","-jar","/app.war"]