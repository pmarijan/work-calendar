# work-calendar API

### How to build and start project using Maven
mvn clean package && java -jar target/work-calendar.jar

or

mvn clean package spring-boot:run
 * to skip tests add "-DskipTests"

#### Project configuration
Project has application.properties configuration file located in src/main/resources folder, where DB connection is configured. 
There are also SQL scripts for h2 or Mysql database initialization, which are executed at spring-boot startup. 

### Docker image
Dockerfile is located in root of the project.
 * Dockerfile SCRIPT WAS NOT TESTED
 
#### How to build docker image (from project root)
docker build -t arctur/work-calendar .

#### Run docker image after build
docker run -p 8080:8080 -t arctur/work-calendar

#### List and stop docker image
docker ps
docker stop <image_name>

### Kubernetes
Yaml configuration script work-calendar-app.yaml is located in root of the project.
 * work-calendar-app.yaml SCRIPT WAS NOT TESTED

#### Run yaml script
kubectl create -f work-calendar-app.yaml

### Swagger UI
Swagger user interface is available at localhost:8080/swagger-ui.html

### TODO
* add missing unit tests

