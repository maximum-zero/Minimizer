package org.maximum0.minimizer.url.ui;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.maximum0.minimizer.testing.IntegrationTestBase;
import org.maximum0.minimizer.url.ui.dto.CreateUrlShortenRequest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class RedirectControllerTest extends IntegrationTestBase {
    @LocalServerPort
    private int port;

    @MockitoSpyBean
    private Clock clock;

    final String originalUrl = "https://www.google.com/search?q=testcontainers";

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void testCreatedAndRedirectOriginalUrl() {
        String shortKey = createShortenUrl(originalUrl);

        given()
                .redirects().follow(false)
                .when()
                .get("/{shortKey}", shortKey)
                .then()
                .statusCode(HttpStatus.FOUND.value())
                .header("Location", equalTo(originalUrl));
    }

    @Test
    void testRedirectNotFound() {
        String notFoundKey = "NonKey";

        given()
                .redirects().follow(false)
                .when()
                .get("/{shortKey}", notFoundKey)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", notNullValue());
    }

    @Test
    void testRedirectExpiredUrl_ShouldReturnNotFoundError() {
        final Instant fixedCreationTime = Instant.now();
        when(clock.instant()).thenReturn(fixedCreationTime);

        String shortKey = createShortenUrl(originalUrl);

        final Instant expiredTime = fixedCreationTime.plus(11, ChronoUnit.DAYS);
        when(clock.instant()).thenReturn(expiredTime);

        given()
                .redirects().follow(false)
                .when()
                .get("/{shortKey}", shortKey)
                .then()
                .statusCode(HttpStatus.GONE.value());
    }

    private String createShortenUrl(String url) {
        CreateUrlShortenRequest request = new CreateUrlShortenRequest(url);

        return given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/urls")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .path("data.short_url");
    }

}