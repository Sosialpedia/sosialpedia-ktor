docker run -d -it --name sosialpedia_db -p 1234:5432 --mount type=bind,source=/opt/sosialpedia/postgresdata/data,target=/var/lib/postgresql/data postgres:latest
docker network create sosialpediaNetwork


