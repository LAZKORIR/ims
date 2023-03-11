
FROM adoptopenjdk/openjdk11:alpine-jre

LABEL maintainer="lazarus Korir"

EXPOSE 8994

VOLUME /tmp

ADD target/ms-notification-service-*.jar ms-notification-service.jar

RUN /bin/sh -c 'touch /ms-notification-service.jar'

ENV TZ=Africa/Nairobi

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ENTRYPOINT ["java","-Xmx256m", "-XX:+UseG1GC", "-Djava.security.egd=file:/dev/./urandom","-jar","/ms-notification-service.jar"]
