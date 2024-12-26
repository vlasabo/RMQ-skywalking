FROM amazoncorretto:21

WORKDIR /app

COPY ./producer/target/*.jar app.jar
COPY ./skywalking-agent /skywalking-agent

ENV JAVA_TOOL_OPTIONS="-javaagent:/skywalking-agent/skywalking-agent.jar"
ENV SW_AGENT_COLLECTOR_BACKEND_SERVICES="skywalking:11800"

ENTRYPOINT ["sh", "-c", "sleep ${STARTUP_DELAY:-30} && java -jar app.jar"]