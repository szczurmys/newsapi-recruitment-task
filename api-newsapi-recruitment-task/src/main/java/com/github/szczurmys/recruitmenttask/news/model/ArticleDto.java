package com.github.szczurmys.recruitmenttask.news.model;

import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;

public class ArticleDto {
    private String author;
    private String title;
    private String description;
    private LocalDate date;
    private String sourceName;
    private String articleUrl;
    private String imageUrl;

    public String getAuthor() {
        return author;
    }

    public ArticleDto setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ArticleDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ArticleDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDate getDate() {
        return date;
    }

    public ArticleDto setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public String getSourceName() {
        return sourceName;
    }

    public ArticleDto setSourceName(String sourceName) {
        this.sourceName = sourceName;
        return this;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public ArticleDto setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ArticleDto setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleDto that = (ArticleDto) o;
        return Objects.equals(author, that.author) &&
                Objects.equals(title, that.title) &&
                Objects.equals(description, that.description) &&
                Objects.equals(date, that.date) &&
                Objects.equals(sourceName, that.sourceName) &&
                Objects.equals(articleUrl, that.articleUrl) &&
                Objects.equals(imageUrl, that.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, title, description, date, sourceName, articleUrl, imageUrl);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ArticleDto{");
        sb.append("author='").append(author).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", date=").append(date);
        sb.append(", sourceName='").append(sourceName).append('\'');
        sb.append(", articleUrl=").append(articleUrl);
        sb.append(", imageUrl=").append(imageUrl);
        sb.append('}');
        return sb.toString();
    }
}
