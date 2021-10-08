FROM adoptopenjdk/openjdk11:latest

COPY Dockerfile /t

CMD ["ls", "-la"]