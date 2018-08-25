package com.github.szczurmys.recruitmenttask.news.model;

import java.util.List;
import java.util.Objects;

public class ArticlesDto {

    private String country;
    private String category;
    private List<ArticleDto> articles;

    public String getCountry() {
        return country;
    }

    public String getCategory() {
        return category;
    }

    public List<ArticleDto> getArticles() {
        return articles;
    }

    public ArticlesDto setCountry(String country) {
        this.country = country;
        return this;
    }

    public ArticlesDto setCategory(String category) {
        this.category = category;
        return this;
    }

    public ArticlesDto setArticles(List<ArticleDto> articles) {
        this.articles = articles;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticlesDto that = (ArticlesDto) o;
        return Objects.equals(country, that.country) &&
                Objects.equals(category, that.category) &&
                Objects.equals(articles, that.articles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, category, articles);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ArticlesDto{");
        sb.append("country='").append(country).append('\'');
        sb.append(", category='").append(category).append('\'');
        sb.append(", articles=").append(articles);
        sb.append('}');
        return sb.toString();
    }
}
