package org.maximum0.minimizer.url.domain;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Url {
    private static final String URL_REGEX =
            "^(https?):\\/\\/" +
            "(([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,})" +
            "(:\\d{1,5})?" +
            "(\\/\\S*)?$";
    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX, Pattern.CASE_INSENSITIVE);

    private final String value;

    public static Url createUrl(String value) {
        return new Url(value);
    }

    private Url(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("URL은 비어있을 수 없습니다.");
        }

        if (!URL_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(value + "는 유효하지 않은 URL 형식입니다.");
        }

        try {
            new URL(value);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(value + "는 유효하지 않은 URL 형식입니다.");
        }

        this.value = value;
    }

}
