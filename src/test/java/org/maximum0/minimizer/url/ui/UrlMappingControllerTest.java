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
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

class UrlMappingControllerTest extends IntegrationTestBase {
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
    void testCreateShortenUrlBySuccess() {
        CreateUrlShortenRequest request = new CreateUrlShortenRequest(originalUrl);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/urls")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("data.short_url", notNullValue());
    }

    @Test
    void testCreateShortenUrlWithInvalidUrlByBadRequest() {
        CreateUrlShortenRequest nullRequest = new CreateUrlShortenRequest(null);

        given()
                .contentType(ContentType.JSON)
                .body(nullRequest)
                .when()
                .post("/api/v1/urls")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", notNullValue());

        CreateUrlShortenRequest invalidFormatRequest = new CreateUrlShortenRequest("Invalid Url");

        given()
                .contentType(ContentType.JSON)
                .body(invalidFormatRequest)
                .when()
                .post("/api/v1/urls")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", notNullValue());
    }

    @Test
    void testCreatedAndRedirectOriginalUrl() {
        String shortKey = createShortenUrl(originalUrl);

        given()
                .redirects().follow(false)
                .when()
                .get("/api/v1/urls/{shortKey}", shortKey)
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
                .get("/api/v1/urls/{shortKey}", notFoundKey)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", notNullValue());
    }

    @Test
    void testRedirectExpiredUrl_ShouldReturnNotFoundError() {
        final Instant fixedCreationTime = Instant.now();
        when(clock.instant()).thenReturn(fixedCreationTime);

        String shortKey = createShortenUrl(originalUrl);
        System.out.println(shortKey);

        final Instant expiredTime = fixedCreationTime.plus(11, ChronoUnit.DAYS);
        when(clock.instant()).thenReturn(expiredTime);

        given()
                .redirects().follow(false)
                .when()
                .get("/api/v1/urls/{shortKey}", shortKey)
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
