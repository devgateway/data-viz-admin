FROM maven:3.8-jdk-11 AS build
RUN mkdir /tcdi-admin
WORKDIR /tcdi-admin
COPY . /tcdi-admin

RUN mvn -B clean package
RUN mkdir -p /opt/tcdi/admin
RUN mv */target/*-0.0.1-SNAPSHOT.jar /opt/tcdi/admin

FROM openjdk:11-jre-slim
WORKDIR /opt/devgateway/tcdi/admin
COPY --from=build /opt/tcdi/admin ./
EXPOSE 8080
ENTRYPOINT ["/opt/devgateway/tcdi/admin/entrypoint.sh"]
