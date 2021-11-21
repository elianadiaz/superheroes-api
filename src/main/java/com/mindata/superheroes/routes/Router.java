package com.mindata.superheroes.routes;

import com.google.gson.Gson;
import com.mindata.superheroes.auth.AuthorizationInterceptor;
import com.mindata.superheroes.auth.Permission;
import com.mindata.superheroes.controllers.SuperheroController;
import com.mindata.superheroes.exceptions.AuthorizationException;
import com.mindata.superheroes.models.Superhero;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import javax.annotation.PostConstruct;

import java.util.List;

import static com.mindata.superheroes.constants.ApiConstants.CONTENT_TYPE;
import static com.mindata.superheroes.constants.RouteConstants.HEADER_TOKEN;
import static spark.Spark.awaitInitialization;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.init;
import static spark.Spark.port;
import static spark.Spark.put;

@Slf4j
@Configuration
public class Router {

    private static final int PORT = 8081;

    private final SuperheroController superheroController;
    private final Gson gson;

    public Router(@Autowired final SuperheroController superheroController) {
        this.superheroController = superheroController;
        this.gson = new Gson();
    }

    @PostConstruct
    private void setUp() {
        port(PORT);
        setUpRoutes();
        init();
        awaitInitialization();
        log.info("Listening on http://localhost:{}/", port());
    }

    private void setUpRoutes() {
        get(Route.PING.getPath(), (request, response) -> {
            response.type(MediaType.TEXT_PLAIN_VALUE);
            return "pong";
        });

        mapSuperheroRoutes();
    }

    private void mapSuperheroRoutes() {
        get(Route.SUPERHEROES.getPath(), (request, response) -> {
            try {
                AuthorizationInterceptor.validateUser(request.headers(HEADER_TOKEN), Permission.VIEW);
            } catch (final AuthorizationException e) {
                response.status(e.getStatus());
                return this.gson.toJson(e, AuthorizationException.class);
            }

            response.type(CONTENT_TYPE);
            return this.gson.toJson(superheroController.getAllSuperheroes(request, response));
        });

        get(Route.SUPERHEROES_ID.getPath(), (request, response) -> {
            try {
                AuthorizationInterceptor.validateUser(request.headers(HEADER_TOKEN), Permission.VIEW);
            } catch (final AuthorizationException e) {
                response.status(e.getStatus());
                return this.gson.toJson(e, AuthorizationException.class);
            }

            response.type(CONTENT_TYPE);
            return this.gson.toJson(superheroController.getSuperheroById(request, response));
        });

        put(Route.SUPERHEROES_ID.getPath(), (request, response) -> {
            try {
                AuthorizationInterceptor.validateUser(request.headers(HEADER_TOKEN), Permission.UPDATE);
            } catch (final AuthorizationException e) {
                response.status(e.getStatus());
                return this.gson.toJson(e, AuthorizationException.class);
            }

            response.type(CONTENT_TYPE);
            return this.gson.toJson(superheroController.updateSuperhero(request, response));
        });

        delete(Route.SUPERHEROES_ID.getPath(), (request, response) -> {
            try {
                AuthorizationInterceptor.validateUser(request.headers(HEADER_TOKEN), Permission.DELETE);
            } catch (final AuthorizationException e) {
                response.status(e.getStatus());
                return this.gson.toJson(e, AuthorizationException.class);
            }

            superheroController.deleteSuperheroById(request, response);
            return this.gson.toJson("{'\"'status\": \"success\"}");
        });
    }

}
