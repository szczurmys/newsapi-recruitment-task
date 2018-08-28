Proxy NewsAPI - Project for recruitment task.
---------------------------------------------

1. Build project <br />
`mvn clean package`
3. Run application in console <br />
`java -jar api-newsapi-recruitment-task/target/api-newsapi-recruitment-task-0.0.1-SNAPSHOT.jar <options>` <br />
Options:
    *  **newsapi.newsApiUrl** - default value: "https://newsapi.org" - address for NewsAPI.
    *  **newsapi.authToken** - authorization token for NewsAPI.
4. Run application in docker
    * build docker image: `mvn clean package dockerfile:build`
    * run image: `szczurmys/newsapi-recruitment-task:0.0.1-SNAPSHOT` <br />
    e.g. `docker run -p 8080:8080 -it szczurmys/newsapi-recruitment-task:0.0.1-SNAPSHOT`  <br />
    Available environment arguments:
      * **NEWSAPI_API_URL** - value for application parameter **newsapi.newsApiUrl**
      * **NEWSAPI_TOKEN** - value for application parameter **newsapi.authToken**
5. WebUI address: `http://localhost:8080/index.html`
6. REST Docs address: `http://localhost:8080/docs/api-guide.html`

