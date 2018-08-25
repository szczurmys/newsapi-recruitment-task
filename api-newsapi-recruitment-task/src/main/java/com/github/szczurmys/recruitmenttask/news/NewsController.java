package com.github.szczurmys.recruitmenttask.news;


import com.github.szczurmys.recruitmenttask.news.model.ArticlesDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController()
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    @GetMapping("/news/{country}/{category}")
    public Mono<ResponseEntity<ArticlesDto>> getArticles(
            @PathVariable(value = "country") String country,
            @PathVariable(value = "category") String category
    ) {
        throw new UnsupportedOperationException("Not implemented yet."); //TODO: implement logic
    }
}
