package com.github.szczurmys.recruitmenttask.news.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsApiSource {
    private String name;

    public String getName() {
        return name;
    }

    public NewsApiSource setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsApiSource that = (NewsApiSource) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "NewsApiSource{" +
                "name='" + name + '\'' +
                '}';
    }
}
