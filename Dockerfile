FROM adoptopenjdk/openjdk11:latest

ARG JAR_FILE=app.jar

COPY ${JAR_FILE} /app.jar

CMD ["java", "-jar", "-DSpring.profiles.active=prod", "app.jar"]