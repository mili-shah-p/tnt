# API Aggregation Service
It is implemented using Spring Web Reactive module. Maven is used to build the project.
The API aggregation service accepts three different parameters to specify the values to be passed to backend APIs

## Backend APIs 

* Pricing API
* Track API
* Shipment API

## Instructions for running application
Project has to be run on local machine from command prompt (and has not been converted into a docker image yet)
Have the docker instances of backend services running on the system.
Then, run the following commands:
* Project build command: `mvn clean install`
* Run application command: `mvn spring-boot:run`

URLs and ports are configured in application.properties file