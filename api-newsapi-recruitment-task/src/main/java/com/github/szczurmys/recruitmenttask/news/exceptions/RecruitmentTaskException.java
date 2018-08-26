package com.github.szczurmys.recruitmenttask.news.exceptions;

public class RecruitmentTaskException extends RuntimeException {
    private static final long serialVersionUID = 3229405123776676096L;

    private final String code;

    public RecruitmentTaskException(String code, String message) {
        super(message);
        this.code = code;
    }

    public RecruitmentTaskException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
