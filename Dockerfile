FROM adoptopenjdk/openjdk11:latest

ADD *.jar app.jar

CMD ["java", "-jar", "-DSpring.profiles.active=prod", "app.jar"]