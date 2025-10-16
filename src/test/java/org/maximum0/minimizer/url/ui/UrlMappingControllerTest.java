package org.maximum0.minimizer.url.ui;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.Clock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.maximum0.minimizer.testing.IntegrationTestBase;
import org.maximum0.minimizer.url.ui.dto.CreateUrlShortenRequest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
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

}
