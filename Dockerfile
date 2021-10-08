FROM adoptopenjdk/openjdk11:latest

COPY *.jar app.jar

CMD ["ls", "-lah", "&&", "whoami"]

CMD ["java", "-jar", "-DSpring.profiles.active=prod", "app.jar"]
