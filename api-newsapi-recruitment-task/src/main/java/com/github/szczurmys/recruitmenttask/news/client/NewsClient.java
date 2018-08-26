package com.github.szczurmys.recruitmenttask.news.client;

import com.github.szczurmys.recruitmenttask.news.client.model.NewsApiResponse;
import reactor.core.publisher.Mono;

public interface NewsClient {
    Mono<NewsApiResponse> getArticles(String country, String category);
}
