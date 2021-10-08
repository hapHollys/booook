FROM adoptopenjdk/openjdk11:latest

COPY *.jar app.jar

CMD ["ls", "-la"]
