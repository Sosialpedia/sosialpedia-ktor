docker run -d -it --name sosialpedia_db -p 1234:5432 --mount type=bind,source=/opt/sosialpedia/postgresdata/data,target=/var/lib/postgresql/data postgres:latest
docker network create sosialpediaNetwork
docker network connect sosialpediaNetwork sosialpedia_db


docker build -t sosialpedia:0.0.1 .
docker container run -d -it --name sosialpedia -p 80:80 sosialpedia

