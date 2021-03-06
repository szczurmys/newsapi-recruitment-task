package com.github.szczurmys.recruitmenttask.news.client;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
@ConfigurationProperties(prefix = "newsapi")
class NewsApiProperties {

    private String newsApiUrl = "https://newsapi.org";

    private String authToken = null;

    public String getNewsApiUrl() {
        return newsApiUrl;
    }

    public NewsApiProperties setNewsApiUrl(String newsApiUrl) {
        this.newsApiUrl = newsApiUrl;
        return this;
    }

    public Optional<String> getAuthToken() {
        return Optional.ofNullable(authToken).map(String::trim).filter(v -> !v.isEmpty());
    }

    public NewsApiProperties setAuthToken(String authToken) {
        this.authToken = authToken;
        return this;
    }
}
