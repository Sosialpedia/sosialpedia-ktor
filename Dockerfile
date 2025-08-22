FROM openjdk:11-jdk
EXPOSE 80
RUN mkdir /app
COPY Sosialpedia-0.0.1-all.jar /app/sosialpedia.jar
ENTRYPOINT ["java","-jar","/app/sosialpedia.jar"]