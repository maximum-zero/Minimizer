package org.maximum0.minimizer.url.ui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record CreateUrlShortenRequest(
        @JsonProperty("original_url")
        @NotBlank(message = "원본 URL은 필수 항목입니다.")
        @URL(message = "유효한 URL 형식이 아닙니다.")
        String originalUrl
) {}
