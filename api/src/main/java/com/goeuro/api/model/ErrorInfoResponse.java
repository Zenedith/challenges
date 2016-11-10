package com.goeuro.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = ErrorInfoResponse.ErrorInfoResponseBuilder.class)
public class ErrorInfoResponse {
    private final String code;
    private final String message;

    private ErrorInfoResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ErrorInfoResponse other = (ErrorInfoResponse) obj;
        return Objects.equals(this.code, other.code)
                && Objects.equals(this.message, other.message);
    }

    @Override
    public String toString() {
        return "ErrorInfoResponse{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonPOJOBuilder
    public static final class ErrorInfoResponseBuilder {
        private String code;
        private String message;

        private ErrorInfoResponseBuilder() {
        }

        public static ErrorInfoResponseBuilder anErrorInfo() {
            return new ErrorInfoResponseBuilder();
        }

        public ErrorInfoResponseBuilder withCode(String code) {
            this.code = code;
            return this;
        }

        public ErrorInfoResponseBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public ErrorInfoResponse build() {
            return new ErrorInfoResponse(code, message);
        }
    }
}

