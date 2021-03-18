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
1. The application requires maven and Java 11 to be installed (and set as JAVA_HOME). In the root of the project execute: *./mvnw clean package*

2. The application also requires docker to be installed locally. Also on the root of the project start up docker-compose with *docker-compose up --build -d*. 
   
To stop the application run *docker-compose down* (with flag -v if we want to erase the database's persistent storage also).

3. The application already is pre loaded with somy dummy data (doctors and patients).

4. Use the application's through the REST API located at ---> [Private-Hospital Swagger API](http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/)

## How to use
1. To start using the API the user must obtain a Jwt token on the Authentication section invoking
   
   POST /authenticate

2. For simplicity in obtaining the token and respective usage of the API two users/passwords were hardcoded (in a real
   world scenario they would simply be stored on a database with their respective passwords hashed.) 

   Username | Password
   ------------ | -------------
   patient | password
   doctor | password

3. After copying the *jwtToken* from the response payload, be sure to paste it on authentication mechanism to login.

3. Play with the API and have fun. :muscle:


### Tested scenarios for business rules
There are 4 patients and 3 doctors on the initial data. (the first has id=1 and so on)

As a patient :mage_woman::
1. Scheduled an appointment: POST /v1/hospital/patients/{id}/appointments

2. Checked doctor's availability: GET /v1/hospital/doctors/{id}/availability :white_check_mark:

As a doctor :man_health_worker::
1. Checked the appointments for a given period: GET /v1/hospital/doctors/{id}/appointments :white_check_mark:

2. Set an absence for a given period: POST /v1/hospital/doctors/{id}/absences :white_check_mark:

*Note:* All the scenarios above were tested successfully, even on the trickier settings i could think of.

### Tested scenarios for security roles

User | Operation
------------ | -------------
patient | POST /v1/hospital/patients/{id}/appointments
patient | GET /v1/hospital/doctors/{id}/availability
doctor |  GET /v1/hospital/doctors/{id}/appointments
doctor |  POST /v1/hospital/doctors/{id}/absences

**Note**: Unauthenticated requests will result in HTTP 401(Unauthorized)
and requests with incorrect permissions will result in HTTP 403(Forbidden). 

## Considerations & Improvements
The overall design of the application has left me satisfied, there are traces of some experiments which can be easily 
refactored and improved, and the main focus, more than using a lot of tools was the correctness of the app itself first.
I think I pretty much covered all the topics proposed in the challenge (if not do let me know), except diving deeper into metrics which for a spring boot app would be only 
configuration and another service on a docker-compose(Elastic, Prometheus, etc...) and connecting them.

Positive points:
- Code cleanliness (i hope)
- Modular approach
- A Restful API design best practices (not HATEOAS, but consistent)
- Enforced best practices for code quality (checkstyle)
- Scalable database design

To improve/objects of possible criticism:
- Additional test coverage (although some I left out due to lack of time and the most important aspects for me are tested)
- Consistency on some code blocks, although not troublesome and are easily fixed (e.g. Dto's and entities, but nothing critical.)
- The pagination used on some endpoints on an ideal scenario would be done on a database but here I wanted the logic on the code so not really a negative.
  (Also I kept the number objects used to an insignificant value so no harm would storm the JVM memory wise).  
- Usage of a database-migration tool like Flyway or Liquibase but not really critical for the purpose of the exercise. 


Let me have your feedback on what needs more improvement. Thanks for the challenge...:thumbsup:

