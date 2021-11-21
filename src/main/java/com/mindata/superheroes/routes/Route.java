package com.mindata.superheroes.routes;

import java.util.regex.Pattern;

import static com.mindata.superheroes.constants.RouteConstants.BASE_SUPERHEROES_API_URL;
import static com.mindata.superheroes.constants.RouteConstants.PARAM_SUPERHERO_ID;

public enum Route {
    PING("/ping"),

    // SUPERHEROES Routes
    SUPERHEROES(BASE_SUPERHEROES_API_URL),
    SUPERHEROES_ID((new StringBuilder(BASE_SUPERHEROES_API_URL)).append("/").append(PARAM_SUPERHERO_ID).toString(),
        BASE_SUPERHEROES_API_URL + "/[^/]+");

    private final String path;
    private final Pattern pattern;

    Route(final String path) {
        this.path = path;
        this.pattern = Pattern.compile(path);
    }

    Route(final String path, final String pattern) {
        this.path = path;
        this.pattern = Pattern.compile(pattern);
    }

    public String getPath() {
        return path;
    }

    public Pattern getPattern() {
        return pattern;
    }
}
