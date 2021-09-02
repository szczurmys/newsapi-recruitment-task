package com.github.szczurmys.recruitmenttask.news;

import com.github.szczurmys.recruitmenttask.news.client.NewsClient;
import com.github.szczurmys.recruitmenttask.news.exceptions.ErrorCode;
import com.github.szczurmys.recruitmenttask.news.exceptions.NewsApiClientException;
import com.github.szczurmys.recruitmenttask.news.exceptions.RecruitmentTaskException;
import com.github.szczurmys.recruitmenttask.news.model.ArticlesDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    private final NewsClient client;
    private final ArticleMapper articleMapper;

    @Autowired
    public NewsController(NewsClient client, ArticleMapper articleMapper) {
        this.client = client;
        this.articleMapper = articleMapper;
    }

    @GetMapping(value = "/news/{country}/{category}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ArticlesDto>> getArticles(
            @PathVariable(value = "country") final String country,
            @PathVariable(value = "category") final String category
    ) {
        return client.getArticles(country, category)
                .map(v -> articleMapper.convertFromNewsApi(v, country, category))
                .map(ResponseEntity::ok);
    }

    @ExceptionHandler(RecruitmentTaskException.class)
    public ResponseEntity<Map<String, String>> catchRecruitmentTaskExceptions(RecruitmentTaskException e) {
        logger.error("Application return exception.", e);
        return new ResponseEntity<>(Map.of(
                "message", e.getMessage(),
                "code", e.getCode()
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NewsApiClientException.class)
    public ResponseEntity<Map<String, String>> catchNewsApiExceptions(NewsApiClientException e) {
        logger.error("NewsApi return exception.", e);
        return new ResponseEntity<>(Map.of(
                "message", e.getMessage(),
                "code", ErrorCode.ERROR_FROM_EXTERNAL_API,
                "externalCode", e.getCode()
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> catchAllExceptions(Exception e) {
        logger.error("RestController return unknown Exception.", e);
        return new ResponseEntity<>(Map.of(
                "message", e.getMessage(),
                "code", ErrorCode.UNKNOWN_ERROR
        ), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
