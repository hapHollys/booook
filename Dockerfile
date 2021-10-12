FROM adoptopenjdk/openjdk11:latest

COPY app.jar /app.jar


CMD ["java", "-Dcom.sun.management.jmxremote.local.only=false", "-Dcom.sun.management.jmxremote.port=1099", "-Dcom.sun.management.jmxremote.ssl=false", "-Dcom.sun.management.jmxremote.authenticate=false", "-Dcom.sun.management.jmxremote", "-Djava.rmi.server.hostname=52.79.80.220", "-Dcom.sun.management.jmxremote.rmi.port=1099", "-DSpring.profiles.active=prod", "-jar", "app.jar"]