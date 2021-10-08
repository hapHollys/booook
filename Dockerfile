FROM adoptopenjdk/openjdk11:latest

ADD build/libs/*.jar app.jar

CMD ["java", "-jar", "-DSpring.profiles.active=prod", "app.jar"]