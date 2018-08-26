package com.github.szczurmys.recruitmenttask.news.builder;

import com.github.szczurmys.recruitmenttask.news.client.model.NewsApiArticle;
import com.github.szczurmys.recruitmenttask.news.client.model.NewsApiSource;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class NewsApiArticleBuilderForTests {

    public static NewsApiArticle filled() {
        return new NewsApiArticle()
                .setSource(new NewsApiSource().setName("github.com/szczurmys"))
                .setAuthor("szczurmys")
                .setTitle("test article")
                .setDescription("Some description")
                .setUrl("https://github.com/szczurmys/newsapi-recruitment-task")
                .setUrlToImage("https://avatars3.githubusercontent.com/u/4983958?s=40&v=4")
                .setPublishedAt(OffsetDateTime.of(2018, 8, 24, 16, 20, 0, 0, ZoneOffset.UTC));
    }

    public static NewsApiArticle filledArticle1() {
        return filled()
                .setTitle("test article 1")
                .setDescription("Some description &#132;to&#324; test 1 &#148;.")
                .setPublishedAt(OffsetDateTime.of(2018, 8, 24, 16, 20, 0, 0, ZoneOffset.UTC));
    }

    public static NewsApiArticle filledArticle2() {
        return filled()
                .setAuthor(null)
                .setTitle("test article 2")
                .setDescription("Some description &#132;to&#324; test 2 &#148;.")
                .setPublishedAt(OffsetDateTime.of(2017, 9, 24, 15, 21,  17, 0, ZoneOffset.UTC));
    }
}
