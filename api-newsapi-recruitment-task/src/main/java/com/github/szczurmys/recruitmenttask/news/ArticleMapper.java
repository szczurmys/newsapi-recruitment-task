package com.github.szczurmys.recruitmenttask.news;

import com.github.szczurmys.recruitmenttask.news.client.model.NewsApiArticle;
import com.github.szczurmys.recruitmenttask.news.client.model.NewsApiResponse;
import com.github.szczurmys.recruitmenttask.news.model.ArticleDto;
import com.github.szczurmys.recruitmenttask.news.model.ArticlesDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
interface ArticleMapper {

    @Mapping(source = "country", target = "country")
    @Mapping(source = "category", target = "category")
    @Mapping(source = "response.articles", target = "articles")
    ArticlesDto convertFromNewsApi(NewsApiResponse response,
                                   String country,
                                   String category);


    @Mapping(source = "source.name", target = "sourceName")
    @Mapping(source = "url", target = "articleUrl")
    @Mapping(source = "urlToImage", target = "imageUrl")
    @Mapping(expression = "java(getDate(article))", target = "date")
    ArticleDto convertArticleFromNewsApi(NewsApiArticle article);

    default LocalDate getDate(NewsApiArticle article) {
        return article.getPublishedAt().map(OffsetDateTime::toLocalDate).orElse(null);
    }
}
