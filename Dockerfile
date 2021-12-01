FROM openjdk:11-jdk

EXPOSE 8080
RUN apt-get -y install tar gzip curl wget unzip
ADD . /app
WORKDIR /app
RUN /app/gradlew clean
RUN /app/gradlew build
RUN mv /app/build/libs/*.war /app/

ENTRYPOINT [ "java", "-jar", "/app/superheros-api-0.0.1-SNAPSHOT.war" ]
