FROM adoptopenjdk/openjdk11:latest

COPY *.jar app.jar

CMD ["java", "-jar", "app.jar"]
