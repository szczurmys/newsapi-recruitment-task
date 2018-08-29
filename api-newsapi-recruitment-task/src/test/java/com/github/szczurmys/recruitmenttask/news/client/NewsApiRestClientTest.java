package com.github.szczurmys.recruitmenttask.news.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.szczurmys.recruitmenttask.news.JacksonConfig;
import com.github.szczurmys.recruitmenttask.news.builder.NewsApiArticleBuilderForTests;
import com.github.szczurmys.recruitmenttask.news.client.model.NewsApiResponse;
import com.github.szczurmys.recruitmenttask.news.exceptions.ErrorCode;
import com.github.szczurmys.recruitmenttask.news.exceptions.NewsApiClientException;
import com.github.szczurmys.recruitmenttask.news.exceptions.RecruitmentTaskException;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NewsApiRestClientTest {

    private WireMockServer mockServer = new NewsApiWireMockConfiguration().createMockServer();
    private ObjectMapper objectMapper = JacksonConfig.configureObjectMapper(new ObjectMapper());
    private NewsApiProperties configuration = mock(NewsApiProperties.class);

    private NewsApiRestClient subject;

    @Before
    public void before() {
        mockServer.start();
        subject = new NewsApiRestClient(
                WebClient.create(mockServer.url("/")),
                configuration,
                objectMapper
        );
    }

    @After
    public void after() {
        mockServer.stop();
    }


    @Test
    public void should_throw_exception_when_token_is_null() {
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
    public void should_throw_exception_when_api_return_error() {
        //given
        String country = "pl";
        String category = "technology";
        String apiKey = "test-token";

        when(configuration.getAuthToken()).thenReturn(Optional.of(apiKey));

        mockServer.stubFor(get(urlEqualTo(String.format("/v2/top-headlines?country=%s&category=%s",
                country, category)))
                .withHeader("Accept", equalTo(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .withHeader("Authorization", equalTo("Bearer " + apiKey))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
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
    public void should_get_articles() {
        //given
        String country = "pl";
        String category = "technology";
        String apiKey = "test-token";

        when(configuration.getAuthToken()).thenReturn(Optional.of(apiKey));

        mockServer.stubFor(get(urlEqualTo(String.format("/v2/top-headlines?country=%s&category=%s",
                country, category)))
                .withHeader("Accept", equalTo(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .withHeader("Authorization", equalTo("Bearer " + apiKey))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .withBodyFile("correct-answer.json")));

        //when
        NewsApiResponse actual = subject.getArticles(country, category).block();

        //then
        assertThat(actual.getStatus()).isEqualTo("ok");
        assertThat(actual.getTotalResults()).isEqualTo(2);
        assertThat(actual.getArticles()).containsOnly(
                NewsApiArticleBuilderForTests.filledArticle1(),
                NewsApiArticleBuilderForTests.filledArticle2()
        );
    }

}