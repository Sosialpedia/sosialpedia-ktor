FROM openjdk:11-jdk
EXPOSE 80
RUN mkdir /app
ENV JWT_SECRET samuelmareno
ENV DB_USERNAME doadmin
ENV DB_PASSWORD AVNS_VSUxv0Q_mWPzD39U1kM
ENV DB_IP sosialpedia-db-do-user-23334893-0.h.db.ondigitalocean.com
ENV DB_PORT 25060
ENV DB_NAME defaultdb
COPY Sosialpedia-0.0.1-all.jar /app/sosialpedia.jar
ENTRYPOINT ["java","-jar","/app/sosialpedia.jar"]