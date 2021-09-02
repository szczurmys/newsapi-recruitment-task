package com.github.szczurmys.recruitmenttask.news.client;

import com.github.szczurmys.recruitmenttask.news.builder.NewsApiArticleBuilderForTests;
import com.github.szczurmys.recruitmenttask.news.client.model.NewsApiResponse;
import com.github.szczurmys.recruitmenttask.news.exceptions.ErrorCode;
import com.github.szczurmys.recruitmenttask.news.exceptions.NewsApiClientException;
import com.github.szczurmys.recruitmenttask.news.exceptions.RecruitmentTaskException;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NewsApiRestClientTest {

    private final WireMockServer mockServer = new NewsApiWireMockConfiguration().createMockServer();
    private final NewsApiProperties configuration = mock(NewsApiProperties.class);

    private NewsApiRestClient subject;

    @BeforeEach
    void before() {
        mockServer.start();
        subject = new NewsApiRestClient(
                WebClient.create(mockServer.url("/")),
                configuration
        );
    }

    @AfterEach
    void after() {
        mockServer.stop();
    }


    @Test
    void should_throw_exception_when_token_is_null() {
        //given
        String country = "pl";
        String category = "technology";
        when(configuration.getAuthToken()).thenReturn(Optional.empty());

        //when
        RecruitmentTaskException actual = (RecruitmentTaskException) catchThrowable(() -> subject.getArticles(country, category).block());

        //then
        assertThat(actual).isInstanceOf(RecruitmentTaskException.class);
        assertThat(actual.getCode()).isEqualTo(ErrorCode.LACK_OF_AUTH_TOKEN);
    }


    @Test
    void should_throw_exception_when_api_return_error() {
        //given
        String country = "pl";
        String category = "technology";
        String apiKey = "test-token";

        when(configuration.getAuthToken()).thenReturn(Optional.of(apiKey));

        mockServer.stubFor(get(urlEqualTo(String.format("/v2/top-headlines?country=%s&category=%s",
                country, category)))
                .withHeader("Accept", equalTo(MediaType.APPLICATION_JSON_VALUE))
                .withHeader("Authorization", equalTo("Bearer " + apiKey))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody("{" +
                                "\"status\": \"error\"," +
                                "\"code\": \"someError\"," +
                                "\"message\": \"Description.\"" +
                                "}")));

        //when
        NewsApiClientException actual = (NewsApiClientException) catchThrowable(() -> subject.getArticles(country, category).block());

        //then
        assertThat(actual)
                .isInstanceOf(NewsApiClientException.class)
                .hasMessage("Description.");
        assertThat(actual.getCode()).isEqualTo("someError");
    }

    @Test
    void should_get_articles() {
        //given
        String country = "pl";
        String category = "technology";
        String apiKey = "test-token";

        when(configuration.getAuthToken()).thenReturn(Optional.of(apiKey));

        mockServer.stubFor(get(urlEqualTo(String.format("/v2/top-headlines?country=%s&category=%s",
                country, category)))
                .withHeader("Accept", equalTo(MediaType.APPLICATION_JSON_VALUE))
                .withHeader("Authorization", equalTo("Bearer " + apiKey))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("correct-answer.json")));

        //when
        NewsApiResponse actual = subject.getArticles(country, category).block();

        //then
        assertThat(actual).isNotNull();
        assertThat(actual.getStatus()).isEqualTo("ok");
        assertThat(actual.getTotalResults()).isEqualTo(2);
        assertThat(actual.getArticles()).containsOnly(
                NewsApiArticleBuilderForTests.filledArticle1(),
                NewsApiArticleBuilderForTests.filledArticle2()
        );
    }

}