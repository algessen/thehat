FROM maven:3.6.3-jdk-11 AS MAVEN_TOOL_CHAIN

ENV PROJECT_DIR_MAVEN=/tmp/server
WORKDIR $PROJECT_DIR_MAVEN

COPY ./pom.xml $PROJECT_DIR_MAVEN
RUN mvn dependency:resolve

COPY ./src/ $PROJECT_DIR_MAVEN/src
RUN mvn install -Dmaven.test.skip=true

FROM openjdk:11

ENV PROJECT_DIR=/opt/server
ENV PROJECT_DIR_MAVEN=/tmp/server
RUN mkdir -p $PROJECT_DIR
WORKDIR $PROJECT_DIR
COPY --from=MAVEN_TOOL_CHAIN $PROJECT_DIR_MAVEN/target/*.jar $PROJECT_DIR/the-hat-game-server.jar

EXPOSE 8080

CMD ["java", "-jar", "-Dspring.redis.host=redis", "/opt/server/the-hat-game-server.jar"]