package com.github.szczurmys.recruitmenttask.news;

import com.github.szczurmys.recruitmenttask.IntegrationTestCategory;
import com.github.szczurmys.recruitmenttask.news.builder.ArticleDtoBuilderForTests;
import com.github.szczurmys.recruitmenttask.news.model.ArticleDto;
import com.github.szczurmys.recruitmenttask.news.model.ArticlesDto;
import com.github.szczurmys.recruitmenttask.news.newsapi.client.NewsApiWireMockConfiguration;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.revinate.assertj.json.JsonPathAssert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = {ApiNewsApiRecruitmentTaskApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(NewsApiWireMockConfiguration.class)
@Category(IntegrationTestCategory.class)
public class ApiNewsApiRecruitmentTaskApplicationTests {

    @Autowired
    private WireMockServer mockServer;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void should_get_articles() throws MalformedURLException {
        //given
        String country = "pl";
        String category = "technology";
        String apiKey = "api0123456789key";

        mockServer.stubFor(get(urlEqualTo(String.format("/v2/top-headlines?country=%s&category=%s&apiKey=%s",
                country, category, apiKey)))
                .withHeader("Accept", equalTo(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .withBodyFile("correct-answer.json")));
        //when
        WebTestClient.ResponseSpec actual = webTestClient
                .get().uri("/news/{country}/{category}", country, category)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange();

        //then
        actual.expectStatus().isOk();

        ArticlesDto actualBody = actual.expectBody(ArticlesDto.class).returnResult().getResponseBody();

        assertThat(actualBody.getCountry()).isEqualTo(country);
        assertThat(actualBody.getCategory()).isEqualTo(category);
        assertThat(actualBody.getArticles())
                .containsOnly(
                        ArticleDtoBuilderForTests.filledArticle1(),
                        ArticleDtoBuilderForTests.filledArticle2()
                );
    }


    @Test
    public void should_return_error_when_something_goes_wrong() throws MalformedURLException {
        //given
        String country = "pl";
        String category = "technology";
        String apiKey = "api0123456789key";

        mockServer.stubFor(get(urlEqualTo(String.format("/v2/top-headlines?country=%s&category=%s&apiKey=%s",
                country, category, apiKey)))
                .withHeader("Accept", equalTo(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .withBody("{" +
                                "\"status\": \"error\"," +
                                "\"code\": \"someError\"," +
                                "\"message\": \"Description.\"" +
                                "}")));
        //when
        WebTestClient.ResponseSpec actual = webTestClient
                .get().uri("/news/{country}/{category}", country, category)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange();

        //then
        actual.expectStatus().is4xxClientError();

        DocumentContext actualBody = JsonPath.parse(actual.expectBody(String.class).returnResult().getResponseBody());

        JsonPathAssert.assertThat(actualBody).jsonPathAsString("$.message").isEqualTo("Unknown error from external api.");
        JsonPathAssert.assertThat(actualBody).jsonPathAsString("$.code").isEqualTo("errorFromExternalApi");
        JsonPathAssert.assertThat(actualBody).jsonPathAsString("$.externalCode").isEqualTo("someError");
    }
}
