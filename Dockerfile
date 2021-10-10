FROM adoptopenjdk/openjdk11:latest

COPY app.jar /app.jar

CMD ["java", "-Dcom.sun.management.jmxremote.local.only=false", "-Dcom.sun.management.jmxremote.authenticate=false", "-Dcom.sun.management.jmxremote.ssl=false", "-Dcom.sun.management.jmxremote.port=8500", "-Dcom.sun.management.jmxremote.rmi.port=8500", "-DSpring.profiles.active=prod", "-jar", "app.jar"]