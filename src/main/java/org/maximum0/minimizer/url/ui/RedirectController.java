package org.maximum0.minimizer.url.ui;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.maximum0.minimizer.url.application.RedirectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RedirectController {
    private final RedirectService redirectService;

    /**
     * Short Key를 이용한 원본 URL로 리다이렉션
     * @param shortKey 단축 URL 키
     */
    @GetMapping("/{shortKey}")
    public ResponseEntity<Void> redirectOriginalUrl(@PathVariable String shortKey) {
        String originalUrl = redirectService.getOriginalUrl(shortKey);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }
}
