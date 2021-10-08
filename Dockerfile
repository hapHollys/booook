FROM adoptopenjdk/openjdk11:latest

COPY *.jar app.jar

CMD ["chmod", "777", "app.jar"]
CMD ["java", "-jar", "-DSpring.profiles.active=prod", "app.jar"]
