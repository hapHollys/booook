FROM adoptopenjdk/openjdk11:latest

COPY build/libs/*.jar app.jar

CMD ["java", "-jar", "-DSpring.profiles.active=prod", "app.jar"]
