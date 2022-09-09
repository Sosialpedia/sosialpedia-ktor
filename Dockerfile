
FROM openjdk:11
EXPOSE 80:80
RUN mkdir /app
ENV JWT_SECRET s3c123t
COPY /*.jar /app/sosialpedia.jar
ENTRYPOINT ["java","-jar","/app/sosialpedia.jar"]