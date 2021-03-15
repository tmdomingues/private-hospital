# The Challenge (WIP)

In this challenge you are asked to develop a backend service to book medical appointments
for a private hospital. The users of this system are the doctors and the patients. The
appointments start from 9am to 7pm, each lasting about 1 hour.

The service exposes a RESTful API to answer the following questions:
1. As a patient, I must be able to see the availability of the doctors and schedule an
   appointment for myself.
2. As a doctor, I must be able to see the appointments that I have for a given time
   period.
3. As a doctor, I can set my self as unavailable for a specific time period. blocking any
   patients from scheduling an appointment for that period. (e.g., doctor can be on
   vacation, sick, etc...)

## How to run locally:
1. The application requires maven and Java 11 to be installed. In the root of the project perform *mvn clean install*

2. The application also requires docker to be installed locally. Start up docker-compose with *docker-compose up*, check optional flags like --detach if desired. 
   To stop the application run *docker-compose down* (with flag -v if we want to erase the database's persistent storage also).

3. The application already comes with some dummy data to test it (doctors and patients).
Check the file under **./docker/private_hospital_init_data.sql** to have data for experimenting the app.

4. Use the application's through the REST API located at ---> [Private-Hospital Swagger API](http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/)


## Considerations & Improvements
(...)