FROM adoptopenjdk/openjdk11:latest

ARG JAR_FILE=*.jar

ADD ${JAR_FILE} app.jar

CMD ["java", "-jar", "-DSpring.profiles.active=prod", "app.jar"]