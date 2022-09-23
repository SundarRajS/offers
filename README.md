# Offers Application

## Overview

The following repo contains - Offers Application

## Guidelines to Run the application.


1. Extract the zip file and get into the code repo folder.

2. Go to the repo folder and run ```mvn clean install```

3. Once the process is completed, there should be the Jar file instead the target folder

4. run the jar file with the command ```java -jar offers-0.0.1-SNAPSHOT.jar```. Application will be running in the port 8001.

## Run the application with docker.

1. Go to the repo folder and build the application with the command ```docker build . -t <tagname>```.

2. start the application with docker using ```docker run -p 8001:8001 <tagname>```


## Source Code

Source code for the example is located in ``/src/main/java/com/customer/offers.``
tests are located in ``/src/test/java/com/customer/offers``

## Test reports

reports are available in ``/offers/target/site/jacoco``

## Run the Offers Application.


1. Go to the application url using http://localhost:8001/offers. 

2. offers are displayed in the application

3. access the individual using http://localhost:8001/offers/1.

4. use POST method http://localhost:8001/offers to create the new offer.

5. cancel the offer using http://localhost:8001/offers/1/cancel

6. Access the API docs using url http://localhost:8001/v3/api-docs/, and swagger UI with http://localhost:8001/swagger-ui/index.html.


