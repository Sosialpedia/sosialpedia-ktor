
FROM openjdk:11
EXPOSE 8080:8080
RUN mkdir /app
COPY /*.jar /app/sosialpedia.jar
ENTRYPOINT ["java","-jar","/app/sosialpedia.jar"]