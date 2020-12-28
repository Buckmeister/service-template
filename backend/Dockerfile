FROM openjdk:11 AS backend-build

# For caching purposes
COPY ./caching/ /caching
WORKDIR /caching
RUN ./mvnw -e clean package

COPY . /backend
WORKDIR /backend
RUN ./mvnw -e clean package

FROM openjdk:11

ARG user=svcuser
ARG group=svcgroup
ARG uid=1337
ARG gid=1337
RUN groupadd -g ${gid} ${group} && useradd -u ${uid} -g ${group} -s /bin/sh ${user}

COPY --from=backend-build /backend/service/target/*.jar /backend/app.jar
USER svcuser:svcgroup
ENTRYPOINT ["java","-jar","/backend/app.jar"]
