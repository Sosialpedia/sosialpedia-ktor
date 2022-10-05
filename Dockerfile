FROM openjdk:11-jdk
EXPOSE 80
RUN mkdir /app
ENV JWT_SECRET s3c123t
COPY build/libs/Sosialpedia-0.0.1-all.jar /app/sosialpedia.jar
ENTRYPOINT ["java","-jar","/app/sosialpedia.jar"]