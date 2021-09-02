package com.github.szczurmys.recruitmenttask.news.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.szczurmys.recruitmenttask.news.client.model.NewsApiResponse;
import com.github.szczurmys.recruitmenttask.news.exceptions.ErrorCode;
import com.github.szczurmys.recruitmenttask.news.exceptions.NewsApiClientException;
import com.github.szczurmys.recruitmenttask.news.exceptions.RecruitmentTaskException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class NewsApiRestClient implements NewsClient {
    private static final Logger logger = LoggerFactory.getLogger(NewsApiRestClient.class);

    private final WebClient client;
    private final NewsApiProperties configuration;

    @Autowired
    public NewsApiRestClient(@Qualifier("newsApiWebClient") WebClient client,
                             NewsApiProperties configuration) {
        this.client = client;
        this.configuration = configuration;
    }

    @Override
    public Mono<NewsApiResponse> getArticles(String country, String category) {

        String token = configuration.getAuthToken()
                .orElseThrow(() -> new RecruitmentTaskException(
                        ErrorCode.LACK_OF_AUTH_TOKEN,
                        "Cannot find api key for NewsApi in configuration."
                ));

        return client.get()
                .uri("/v2/top-headlines?country={country}&category={category}", country, category)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> clientResponse
                        .bodyToMono(JsonNode.class)
                        .map(node -> {
                            String code = node.get("code").asText();
                            String message = node.get("message").asText();
                            return new NewsApiClientException(clientResponse.statusCode(), code, message);
                        })
                        .onErrorResume(Exception.class, e -> {
                            logger.error("Error when try parse GitHub error body response.", e);
                            return Mono.just(new NewsApiClientException(clientResponse.statusCode(),
                                    ErrorCode.ERROR_WHEN_TRY_READ_ERROR_MESSAGE,
                                    e.getMessage(),
                                    e));
                        })
                )
                .bodyToMono(NewsApiResponse.class);
    }
}
