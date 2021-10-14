FROM adoptopenjdk/openjdk11:latest

COPY app.jar /app.jar

CMD ["java", "-DSpring.profiles.active=prod", "-jar", "app.jar"]