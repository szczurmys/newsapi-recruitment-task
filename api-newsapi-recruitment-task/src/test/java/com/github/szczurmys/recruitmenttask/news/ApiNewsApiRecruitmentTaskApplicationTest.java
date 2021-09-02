package com.github.szczurmys.recruitmenttask.news;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.szczurmys.recruitmenttask.TestTags;
import com.github.szczurmys.recruitmenttask.news.builder.ArticleDtoBuilderForTests;
import com.github.szczurmys.recruitmenttask.news.client.NewsApiWireMockConfiguration;
import com.github.szczurmys.recruitmenttask.news.model.ArticlesDto;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.http.Fault;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = {ApiNewsApiRecruitmentTaskApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(NewsApiWireMockConfiguration.class)
@Tag(TestTags.INTEGRATION_TEST)
class ApiNewsApiRecruitmentTaskApplicationTest {
    private static final ObjectMapper OBJECT_MAPPER = JacksonConfig.configureObjectMapper(new ObjectMapper());

    @Autowired
    private WireMockServer mockServer;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void should_get_articles() {
        //given
        String country = "pl";
        String category = "technology";
        String apiKey = "test-token";

        mockServer.stubFor(get(urlEqualTo(String.format("/v2/top-headlines?country=%s&category=%s",
                country, category)))
                .withHeader("Accept", equalTo(MediaType.APPLICATION_JSON_VALUE))
                .withHeader("Authorization", equalTo("Bearer " + apiKey))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("correct-answer.json")));
        //when
        WebTestClient.ResponseSpec actual = webTestClient
                .get().uri("/news/{country}/{category}", country, category)
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        //then
        actual.expectStatus().isOk();

        ArticlesDto actualBody = actual.expectBody(ArticlesDto.class).returnResult().getResponseBody();

        assertThat(actualBody).isNotNull();
        assertThat(actualBody.getCountry()).isEqualTo(country);
        assertThat(actualBody.getCategory()).isEqualTo(category);
        assertThat(actualBody.getArticles())
                .containsOnly(
                        ArticleDtoBuilderForTests.filledArticle1(),
                        ArticleDtoBuilderForTests.filledArticle2()
                );
    }


    @Test
    void should_return_error_when_something_goes_wrong() throws JsonProcessingException {
        //given
        String country = "pl";
        String category = "technology";
        String apiKey = "test-token";

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
        WebTestClient.ResponseSpec actual = webTestClient
                .get().uri("/news/{country}/{category}", country, category)
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        //then
        actual.expectStatus().is4xxClientError();

        JsonNode actualBody = OBJECT_MAPPER.readTree(actual.expectBody(String.class).returnResult().getResponseBody());

        assertThat(actualBody).isNotNull();
        assertThat(actualBody.get("message").asText()).isEqualTo("Description.");
        assertThat(actualBody.get("code").asText()).isEqualTo("errorFromExternalApi");
        assertThat(actualBody.get("externalCode").asText()).isEqualTo("someError");
    }

    @Test
    void should_return_error_when_news_api_connection_return_connection_exception() throws JsonProcessingException {
        //given
        String country = "pl";
        String category = "technology";
        String apiKey = "test-token";

        mockServer.stubFor(get(urlEqualTo(String.format("/v2/top-headlines?country=%s&category=%s",
                country, category)))
                .withHeader("Accept", equalTo(MediaType.APPLICATION_JSON_VALUE))
                .withHeader("Authorization", equalTo("Bearer " + apiKey))
                .willReturn(aResponse()
                        .withFault(Fault.MALFORMED_RESPONSE_CHUNK))
        );
        //when
        WebTestClient.ResponseSpec actual = webTestClient
                .get().uri("/news/{country}/{category}", country, category)
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        //then
        actual.expectStatus().is5xxServerError();

        JsonNode actualBody = OBJECT_MAPPER.readTree(actual.expectBody(String.class).returnResult().getResponseBody());

        assertThat(actualBody).isNotNull();
        assertThat(actualBody.get("message").asText()).isEqualTo("Connection prematurely closed DURING response");
        assertThat(actualBody.get("code").asText()).isEqualTo("unknownError");
    }
}
