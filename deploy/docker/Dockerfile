FROM ubuntu:16.04

MAINTAINER shihy

RUN mkdir -p /app/config /app/logs
VOLUME /app
WORKDIR /app

ENV JAVA_HOME=/usr/local/jdk \
    CLASSPATH=/usr/local/jdk/lib:/usr/local/jdk/jre/lib \
    PATH=${PATH}:/usr/local/jdk/bin


ADD jdk.tar /usr/local/
ADD encrypt.txt /usr/local/
ADD libhasp.tar /usr/lib

ADD face-0.0.1-SNAPSHOT.jar /app
ENV LC_ALL C.UTF-8
ENV LANG C.UTF-8
EXPOSE 48861

ENTRYPOINT ["java","-jar","face-0.0.1-SNAPSHOT.jar","--Dspring.config.location=config/application-dev.yml"]