#FROM ubuntu:16.04
FROM java:8

# Maintainer
MAINTAINER "grajendra@iasys.co.in"
WORKDIR /usr/local/
COPY . /usr/local/
EXPOSE 8092
CMD ["java","-jar","/usr/local/GraphSearchPortal.jar"]