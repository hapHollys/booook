FROM adoptopenjdk/openjdk11:latest

COPY build/libs/*.jar app.jar

CMD ["ls", "-la"]
