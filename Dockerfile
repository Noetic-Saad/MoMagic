FROM openjdk:11

ENV TZ=Asia/Karachi
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

WORKDIR /opt/app

COPY target/mo-sptrans-0.0.1-SNAPSHOT.jar .
#COPY log4j.properties .
#COPY database.properties .

EXPOSE 10004

ENTRYPOINT ["java" , "-jar" , "mo-sptrans-0.0.1-SNAPSHOT.jar"]
CMD [""]
