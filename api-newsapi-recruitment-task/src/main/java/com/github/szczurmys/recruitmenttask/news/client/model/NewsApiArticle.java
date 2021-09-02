package com.github.szczurmys.recruitmenttask.news.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsApiArticle {

    private NewsApiSource source;
    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private OffsetDateTime publishedAt;

    public NewsApiSource getSource() {
        return source;
    }

    public NewsApiArticle setSource(NewsApiSource source) {
        this.source = source;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public NewsApiArticle setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public NewsApiArticle setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public NewsApiArticle setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public NewsApiArticle setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public NewsApiArticle setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
        return this;
    }

    public Optional<OffsetDateTime> getPublishedAt() {
        return Optional.ofNullable(publishedAt);
    }

    public NewsApiArticle setPublishedAt(OffsetDateTime publishedAt) {
        this.publishedAt = publishedAt;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsApiArticle that = (NewsApiArticle) o;
        return Objects.equals(source, that.source) &&
                Objects.equals(author, that.author) &&
                Objects.equals(title, that.title) &&
                Objects.equals(description, that.description) &&
                Objects.equals(url, that.url) &&
                Objects.equals(urlToImage, that.urlToImage) &&
                Objects.equals(publishedAt, that.publishedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, author, title, description, url, urlToImage, publishedAt);
    }

    @Override
    public String toString() {
        return "NewsApiArticle{" +
                "source=" + source +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                ", publishedAt=" + publishedAt +
                '}';
    }
}
