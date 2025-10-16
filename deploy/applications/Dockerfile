ARG JDK=registry.cn-hangzhou.aliyuncs.com/xtechcn-public/openjdk:21-jdk
FROM ${JDK}

ARG TIME_ZONE
ARG SERVER_PORT=1123

ARG SERVER_NAME
ENV NAME=${SERVER_NAME}

ENV JAVA_OPTS="-Xms256m -Xmx512m -Djava.security.egd=file:/dev/./urandom"

RUN ln -sf /usr/share/zoneinfo/$TIME_ZONE /etc/localtime && echo $TIME_ZONE > /etc/timezone

RUN mkdir -p /${SERVER_NAME}

WORKDIR /${SERVER_NAME}

EXPOSE ${SERVER_PORT}

ADD ./${SERVER_NAME}.jar ./

CMD sleep 5;java $JAVA_OPTS -jar ${NAME}.jar