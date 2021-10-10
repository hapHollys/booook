FROM adoptopenjdk/openjdk11:latest

COPY app.jar /app.jar

CMD ["java", "-jar", "-Dcom.sun.management.jmxremote.local.only=false", "-Dcom.sun.management.jmxremote.authenticate=false", "-Dcom.sun.management.jmxremote.ssl=false", "-Dcom.sun.management.jmxremote.port=8500", "-Dcom.sun.management.jmxremote.rmi.port=8500", "-Djava.rmi.server.hostname=3.35.0.126", "-DSpring.profiles.active=prod", "app.jar"]