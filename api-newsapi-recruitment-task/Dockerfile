FROM openjdk:10
MAINTAINER Jakub Goszczurny <szczurmys@o2.pl>

ARG JAR_FILE
ADD target/${JAR_FILE} /opt/api-newsapi-recruitment-task.jar

RUN echo "${JAR_FILE}"

ENV NEWSAPI_API_URL=https://newsapi.org
ENV NEWSAPI_TOKEN=""


WORKDIR /opt
CMD if [ ! -z "${NEWSAPI_API_URL}" ]; then URL_PARAM="--newsapi.newsApiUrl=${NEWSAPI_API_URL}"; fi; \
    if [ ! -z "${NEWSAPI_TOKEN}" ]; then TOKEN_PARAM="--newsapi.authToken=${NEWSAPI_TOKEN}"; fi; \
    java -jar ./api-newsapi-recruitment-task.jar ${URL_PARAM} ${TOKEN_PARAM}

EXPOSE 8080