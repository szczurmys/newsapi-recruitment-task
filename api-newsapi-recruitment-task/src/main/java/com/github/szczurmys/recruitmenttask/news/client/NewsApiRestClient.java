package com.github.szczurmys.recruitmenttask.news.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.szczurmys.recruitmenttask.news.client.model.NewsApiResponse;
import com.github.szczurmys.recruitmenttask.news.exceptions.ErrorCode;
import com.github.szczurmys.recruitmenttask.news.exceptions.NewsApiClientException;
import com.github.szczurmys.recruitmenttask.news.exceptions.RecruitmentTaskException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Component
public class NewsApiRestClient implements NewsClient {
    private static final Logger logger = LoggerFactory.getLogger(NewsApiRestClient.class);

    private WebClient client;
    private NewsApiConfiguration configuration;
    private ObjectMapper objectMapper;

    @Autowired
    public NewsApiRestClient(@Qualifier("newsApiWebClient") WebClient client,
                             NewsApiConfiguration configuration,
                             ObjectMapper objectMapper) {
        this.client = client;
        this.configuration = configuration;
        this.objectMapper = objectMapper;
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
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(NewsApiResponse.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    try {
                        JsonNode actualObj = objectMapper.readTree(e.getResponseBodyAsByteArray());
                        String code = actualObj.get("code").asText();
                        String message = actualObj.get("message").asText();
                        throw new NewsApiClientException(e.getStatusCode(), code, message, e);
                    } catch (IOException ex) {
                        logger.error("Error when try parse GitHub error body response.", ex);
                        throw new NewsApiClientException(e.getStatusCode(),
                                ErrorCode.ERROR_WHEN_TRY_READ_ERROR_MESSAGE,
                                ex.getMessage(),
                                e);
                    }
                });
    }
}
