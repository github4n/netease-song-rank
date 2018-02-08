FROM livingobjects/jre8
VOLUME /tmp
ADD biz-user-1.0.0.jar app.jar
RUN bash -c 'touch /app.jar'
ENV JAVA_OPTS="-Xmx1g"
ENTRYPOINT java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /app.jar
