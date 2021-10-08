FROM adoptopenjdk/openjdk11:latest

COPY app.jar /t.jar

CMD ["java", "-jar", "-DSpring.profiles.active=prod", "app.jar"]