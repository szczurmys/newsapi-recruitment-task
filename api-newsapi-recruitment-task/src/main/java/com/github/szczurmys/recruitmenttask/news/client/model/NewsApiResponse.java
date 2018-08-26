package com.github.szczurmys.recruitmenttask.news.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsApiResponse {
    private String status;
    private Integer totalResults;
    private List<NewsApiArticle> articles;

    public String getStatus() {
        return status;
    }

    public NewsApiResponse setStatus(String status) {
        this.status = status;
        return this;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public NewsApiResponse setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
        return this;
    }

    public List<NewsApiArticle> getArticles() {
        return articles;
    }

    public NewsApiResponse setArticles(List<NewsApiArticle> articles) {
        this.articles = articles;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsApiResponse that = (NewsApiResponse) o;
        return Objects.equals(status, that.status) &&
                Objects.equals(totalResults, that.totalResults) &&
                Objects.equals(articles, that.articles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, totalResults, articles);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("NewsApiResponse{");
        sb.append("status='").append(status).append('\'');
        sb.append(", totalResults=").append(totalResults);
        sb.append(", articles=").append(articles);
        sb.append('}');
        return sb.toString();
    }
}
