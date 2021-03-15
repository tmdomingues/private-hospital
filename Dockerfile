FROM adoptopenjdk/openjdk11:alpine-slim AS jlink

COPY target/private-hospital-0.0.1-SNAPSHOT.jar /private-hospital.jar
CMD ["java", "-jar", "/private-hospital.jar"]
