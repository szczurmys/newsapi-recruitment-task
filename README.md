Proxy NewsAPI - Project for recruitment task.
---------------------------------------------

1. Build project and generate REST Docs<br />
`mvn clean package`
1. Build project, generate REST Docs and run integration tests<br />
`mvn clean verify`
3. Run application in console <br />
`java -jar api-newsapi-recruitment-task/target/api-newsapi-recruitment-task-0.0.1-SNAPSHOT.jar <options>` <br />
Options:
    *  **newsapi.newsApiUrl** - default value: "https://newsapi.org" - address for NewsAPI.
    *  **newsapi.authToken** - required; authorization token for NewsAPI.
4. Run application in docker
    * build docker image: `mvn clean package dockerfile:build`
    * run image: `szczurmys/newsapi-recruitment-task:0.0.1-SNAPSHOT` <br />
    e.g. `docker run --init -p 8080:8080 -e NEWSAPI_TOKEN=a0b1c2d3e4f5g6h7i9j -it szczurmys/newsapi-recruitment-task:0.0.1-SNAPSHOT`  <br />
    Available environment arguments:
      * **NEWSAPI_API_URL** - value for application parameter **newsapi.newsApiUrl**
      * **NEWSAPI_TOKEN** - required; value for application parameter **newsapi.authToken**
5. WebUI address: `http://localhost:8080/index.html`
6. REST Docs address: `http://localhost:8080/docs/api-guide.html`

