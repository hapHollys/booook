FROM adoptopenjdk/openjdk11:latest

COPY app.jar /

CMD ["java", "-jar", "-DSpring.profiles.active=prod", "app.jar"]