
FROM openjdk:11
EXPOSE 80:80
RUN mkdir /app
COPY /*.jar /app/sosialpedia.jar
ENTRYPOINT ["java","-jar","/app/sosialpedia.jar"]