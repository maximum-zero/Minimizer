package org.maximum0.minimizer.url.ui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateUrlShortenResponse(
        @JsonProperty("short_url")
        String shortUrl
) {}
