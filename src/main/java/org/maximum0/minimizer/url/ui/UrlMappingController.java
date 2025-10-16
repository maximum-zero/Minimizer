package org.maximum0.minimizer.url.ui;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.maximum0.minimizer.common.response.Response;
import org.maximum0.minimizer.url.application.UrlService;
import org.maximum0.minimizer.url.domain.UrlMapping;
import org.maximum0.minimizer.url.ui.dto.CreateUrlShortenRequest;
import org.maximum0.minimizer.url.ui.dto.CreateUrlShortenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UrlMappingController {
    private final UrlService urlService;

    /**
     * 단축 URL 생성
     */
    @PostMapping("/urls")
    public ResponseEntity<Response<CreateUrlShortenResponse>> createShortenUrl(@RequestBody @Valid CreateUrlShortenRequest request) {
        UrlMapping urlMapping = urlService.createShortenUrl(request.originalUrl());
        return Response.created(new CreateUrlShortenResponse(urlMapping.getShortKey()));
    }

}
