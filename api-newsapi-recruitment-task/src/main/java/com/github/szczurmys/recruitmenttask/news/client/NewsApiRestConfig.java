package com.github.szczurmys.recruitmenttask.news.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
class NewsApiRestConfig {

    @Bean(name = "newsApiWebClient")
    public WebClient createGithubRestClient(NewsApiProperties configuration) {
        return WebClient.create(configuration.getNewsApiUrl());
    }
}
