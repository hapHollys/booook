FROM adoptopenjdk/openjdk11:latest

COPY t.jar /

CMD ["java", "-jar", "-DSpring.profiles.active=prod", "app.jar"]