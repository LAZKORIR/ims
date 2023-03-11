
FROM adoptopenjdk/openjdk11:alpine-jre

LABEL maintainer="lazarus Korir"

EXPOSE 8994

VOLUME /tmp

ADD target/ms-loan-repayment-service-*.jar ms-loan-repayment-service.jar

RUN /bin/sh -c 'touch /ms-loan-repayment-service.jar'

ENV TZ=Africa/Nairobi

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ENTRYPOINT ["java","-Xmx256m", "-XX:+UseG1GC", "-Djava.security.egd=file:/dev/./urandom","-jar","/ms-loan-repayment-service.jar"]
