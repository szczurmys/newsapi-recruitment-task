package com.github.szczurmys.recruitmenttask.news.exceptions;

import org.springframework.http.HttpStatus;

public class NewsApiClientException extends RuntimeException {
    private static final long serialVersionUID = 3229405123776676096L;

    private final HttpStatus httpStatus;
    private final String code;

    public NewsApiClientException(HttpStatus httpStatus, String code, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.code = code;
    }

    public NewsApiClientException(HttpStatus httpStatus, String code, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.code = code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getCode() {
        return code;
    }
}
