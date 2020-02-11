#base image for docker image creation
FROM openjdk:8-jdk-alpine

LABEL maintainer=Primoz Marijan
LABEL description=Docker image created to run spring-boot WorkCalendar application

#create mountpoint
#VOLUME /tmp

#add new user and group to linux
RUN addgroup -S pmarijan && adduser -S pmarijan -G pmarijan

#set with wich user we want to work when ssh to image
USER pmarijan:pmarijan

#set target folder to variable
ARG JAR_FILE=target/*.jar

#copy project files to container
COPY ${JAR_FILE} work-calendar.jar

#port which should be exposed to public
EXPOSE 8080

#execute spring-boot application
ENTRYPOINT ["java","-jar","/work-calendar.jar"]