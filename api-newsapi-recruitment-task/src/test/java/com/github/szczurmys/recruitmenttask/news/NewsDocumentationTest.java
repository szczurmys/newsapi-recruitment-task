package com.github.szczurmys.recruitmenttask.news;

import com.github.szczurmys.recruitmenttask.news.client.NewsClient;
import com.github.szczurmys.recruitmenttask.news.client.model.NewsApiResponse;
import com.github.szczurmys.recruitmenttask.news.exceptions.ErrorCode;
import com.github.szczurmys.recruitmenttask.news.exceptions.NewsApiClientException;
import com.github.szczurmys.recruitmenttask.news.exceptions.RecruitmentTaskException;
import com.github.szczurmys.recruitmenttask.news.model.ArticleDto;
import com.github.szczurmys.recruitmenttask.news.model.ArticlesDto;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.operation.OperationResponse;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;


@SpringBootApplication
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {NewsDocumentationTest.class, NewsController.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NewsDocumentationTest {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    @MockBean
    private NewsClient newsClient;

    @MockBean
    private ArticleMapper articleMapper;

    @Autowired
    private ApplicationContext context;

    private WebTestClient webTestClient;

    @Before
    public void setUp() {
        this.webTestClient = WebTestClient.bindToApplicationContext(context)
                .configureClient().baseUrl("https://api.example.com")
                .filter(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    public void getArticles() throws Exception {
        String country = "pl";
        String category = "technology";

        when(newsClient.getArticles(any(), any())).thenReturn(Mono.just(new NewsApiResponse()));
        when(articleMapper.convertFromNewsApi(any(), any(), any())).thenReturn(
                new ArticlesDto()
                        .setCountry(country)
                        .setCategory(category)
                        .setArticles(Arrays.asList(
                                new ArticleDto()
                                        .setAuthor("Author")
                                        .setDate(LocalDate.of(2018, 8, 28))
                                        .setTitle("First news for documentation")
                                        .setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                                                "Praesent sodales ultricies nisi scelerisque ornare.")
                                        .setArticleUrl("https://github.com/szczurmys")
                                        .setImageUrl("https://avatars0.githubusercontent.com/u/4983958?s=460&v=4")
                                        .setSourceName("GitHub"),
                                new ArticleDto()
                                        .setAuthor(null)
                                        .setDate(LocalDate.of(2018, 8, 28))
                                        .setTitle("Second news for documentation")
                                        .setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                                                "Praesent sodales ultricies nisi scelerisque ornare.")
                                        .setArticleUrl("https://github.com/szczurmys")
                                        .setImageUrl("https://avatars0.githubusercontent.com/u/4983958?s=460&v=4")
                                        .setSourceName("GitHub")
                        ))
        );

        webTestClient.get().uri("/news/{country}/{category}", country, category)
                .accept(MediaType.APPLICATION_JSON_UTF8, MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(
                        document("news",
                                new OperationResponsePreprocessor() {
                                    @Override
                                    public OperationResponse preprocess(OperationResponse operationResponse) {
                                        return Preprocessors.prettyPrint().preprocess(operationResponse);
                                    }
                                },
                                pathParameters(
                                        parameterWithName("country")
                                                .description("The country from which news will be. Available: " +
                                                        "ae ar at au be bg br ca ch cn co cu cz de eg fr gb gr hk hu id ie il in it jp kr lt lv ma mx my ng nl no nz ph pl pt ro rs ru sa se sg si sk th tr tw ua us ve za"),
                                        parameterWithName("category").description("The category from which news will be. Available: " +
                                                "business entertainment general health science sports technology")
                                ),
                                responseFields(
                                        fieldWithPath("country")
                                                .type(JsonFieldType.STRING)
                                                .description("The country from which news are"),
                                        fieldWithPath("category")
                                                .type(JsonFieldType.STRING)
                                                .description("The category from which news are"),
                                        fieldWithPath("articles.[]")
                                                .type(JsonFieldType.ARRAY)
                                                .description("News for the specified country and category."),
                                        fieldWithPath("articles.[].author")
                                                .optional()
                                                .description("Author of the news."),
                                        fieldWithPath("articles.[].title")
                                                .type(JsonFieldType.STRING)
                                                .description("Title of the news."),
                                        fieldWithPath("articles.[].description")
                                                .type(JsonFieldType.STRING)
                                                .description("News content."),
                                        fieldWithPath("articles.[].date")
                                                .type(JsonFieldType.STRING + "(yyyy-MM-dd)")
                                                .description("The date of the news."),
                                        fieldWithPath("articles.[].sourceName")
                                                .type(JsonFieldType.STRING)
                                                .description("The source name of the news."),
                                        fieldWithPath("articles.[].articleUrl")
                                                .type(JsonFieldType.STRING)
                                                .description("URL address of the news."),
                                        fieldWithPath("articles.[].imageUrl")
                                                .type(JsonFieldType.STRING)
                                                .description("URL image of the news.")

                                )
                        )
                );

        return;
    }

    @Test
    public void getArticlesWithServerException() throws Exception {
        String country = "pl";
        String category = "technology";

        when(newsClient.getArticles(any(), any())).thenThrow(new RecruitmentTaskException(
                ErrorCode.LACK_OF_AUTH_TOKEN,
                "Cannot find api key for NewsApi in configuration."
        ));


        webTestClient.get().uri("/news/{country}/{category}", country, category)
                .accept(MediaType.APPLICATION_JSON_UTF8, MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .consumeWith(
                        document("news-server-error",
                                new OperationResponsePreprocessor() {
                                    @Override
                                    public OperationResponse preprocess(OperationResponse operationResponse) {
                                        return Preprocessors.prettyPrint().preprocess(operationResponse);
                                    }
                                },
                                responseFields(
                                        fieldWithPath("message")
                                                .type(JsonFieldType.STRING)
                                                .description("Message of the error."),
                                        fieldWithPath("code")
                                                .type(JsonFieldType.STRING)
                                                .description("Code of the error.")
                                )
                        )
                );

        return;
    }

    @Test
    public void getArticlesWithNewsApiException() throws Exception {
        String country = "pl";
        String category = "technology";

        when(newsClient.getArticles(any(), any())).thenThrow(new NewsApiClientException(
                HttpStatus.BAD_REQUEST,
                "apiKeyMissing",
                "Your API key is missing from the request."
        ));


        webTestClient.get().uri("/news/{country}/{category}", country, category)
                .accept(MediaType.APPLICATION_JSON_UTF8, MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .consumeWith(
                        document("news-newsapi-error",
                                new OperationResponsePreprocessor() {
                                    @Override
                                    public OperationResponse preprocess(OperationResponse operationResponse) {
                                        return Preprocessors.prettyPrint().preprocess(operationResponse);
                                    }
                                },
                                responseFields(
                                        fieldWithPath("message")
                                                .type(JsonFieldType.STRING)
                                                .description("Message of the error."),
                                        fieldWithPath("code")
                                                .type(JsonFieldType.STRING)
                                                .description("Code of the error."),
                                        fieldWithPath("externalCode")
                                                .type(JsonFieldType.STRING)
                                                .description("NewsAPI code of the error.")
                                )
                        )
                );

        return;
    }

    @Test
    public void getArticlesWithUnknownException() throws Exception {
        String country = "pl";
        String category = "technology";

        when(newsClient.getArticles(any(), any())).thenThrow(new NullPointerException(
                "Some parameter is null."
        ));


        webTestClient.get().uri("/news/{country}/{category}", country, category)
                .accept(MediaType.APPLICATION_JSON_UTF8, MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .consumeWith(
                        document("news-unknown-error",
                                new OperationResponsePreprocessor() {
                                    @Override
                                    public OperationResponse preprocess(OperationResponse operationResponse) {
                                        return Preprocessors.prettyPrint().preprocess(operationResponse);
                                    }
                                },
                                responseFields(
                                        fieldWithPath("message")
                                                .type(JsonFieldType.STRING)
                                                .description("Message of the error."),
                                        fieldWithPath("code")
                                                .type(JsonFieldType.STRING)
                                                .description("Code of the error.")
                                )
                        )
                );

        return;
    }

}