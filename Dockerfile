FROM ubuntu:18.04
MAINTAINER dileep.jallipalli@hyscale.io
RUN apt-get update -y && apt-get upgrade -y

FROM alpine/git
WORKDIR /app
RUN git clone https://github.com/dileep-294/cricket-webAPP.git

FROM maven:3.5-jdk-8-alpine
WORKDIR /app
COPY --from=1 /app/cricket-webAPP /app
RUN mvn clean install
