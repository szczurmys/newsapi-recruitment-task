package com.github.szczurmys.recruitmenttask.news.builder;

import com.github.szczurmys.recruitmenttask.news.model.ArticleDto;

import java.time.LocalDate;

public class ArticleDtoBuilderForTests {

    public static ArticleDto filled() {
        return new ArticleDto()
                .setSourceName("github.com/szczurmys")
                .setAuthor("szczurmys")
                .setTitle("test article")
                .setDescription("Some description")
                .setArticleUrl("https://github.com/szczurmys/newsapi-recruitment-task")
                .setImageUrl("https://avatars3.githubusercontent.com/u/4983958?s=40&v=4")
                .setDate(LocalDate.of(2018, 8, 24));
    }

    public static ArticleDto filledArticle1() {
        return filled()
                .setTitle("test article 1")
                .setDescription("Some description &#132;to&#324; test 1 &#148;.")
                .setDate(LocalDate.of(2018, 8, 24));
    }

    public static ArticleDto filledArticle2() {
        return filled()
                .setAuthor(null)
                .setTitle("test article 2")
                .setDescription("Some description &#132;to&#324; test 2 &#148;.")
                .setDate(LocalDate.of(2017, 9, 24));
    }
}
