FROM docker.io/library/eclipse-temurin:21-jre-alpine
WORKDIR /opt/devgateway/tcdi/admin
COPY target/deps/BOOT-INF/lib lib
COPY target/deps/META-INF META-INF
COPY entrypoint.sh ./
COPY target/deps/BOOT-INF/classes .
#USER nobody
EXPOSE 8080
ENTRYPOINT ["/opt/devgateway/tcdi/admin/entrypoint.sh"]
CMD ["admin"]
