package ru.tinkoff.edu.java.scrapper.dto.response;

public class ApiErrorResponse {
    private String description;
    private String code;

    private String exceptionName;
    private String exceptionMessage;
    private String[] stacktrace;

    public ApiErrorResponse() {
    }

    public ApiErrorResponse(String description,
                            String code,
                            String exceptionName,
                            String exceptionMessage,
                            String[] stacktrace) {
        this.description = description;
        this.code = code;
        this.exceptionName = exceptionName;
        this.exceptionMessage = exceptionMessage;
        this.stacktrace = stacktrace;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getExceptionName() {
        return exceptionName;
    }

    public void setExceptionName(String exceptionName) {
        this.exceptionName = exceptionName;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public String[] getStacktrace() {
        return stacktrace;
    }

    public void setStacktrace(String[] stacktrace) {
        this.stacktrace = stacktrace;
    }
}
