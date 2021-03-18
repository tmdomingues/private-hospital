FROM adoptopenjdk/openjdk11:alpine-slim AS jlink

COPY target/*.jar /private-hospital.jar
CMD ["java", "-jar", "/private-hospital.jar"]
