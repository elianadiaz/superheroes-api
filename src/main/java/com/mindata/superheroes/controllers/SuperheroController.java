package com.mindata.superheroes.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mindata.superheroes.entities.Filter;
import com.mindata.superheroes.exceptions.SuperheroException;
import com.mindata.superheroes.models.Superhero;
import com.mindata.superheroes.services.SuperheroService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import spark.Request;
import spark.Response;

import java.util.List;

import static com.mindata.superheroes.constants.RouteConstants.QUERY_PARAM_WORD_IN_NAME;
import static com.mindata.superheroes.constants.RouteConstants.PARAM_SUPERHERO_ID;

@RestController
public class SuperheroController {

    private final SuperheroService superheroService;
    private final Gson gson;

    public SuperheroController(@Autowired final SuperheroService superheroService) {
        this.superheroService = superheroService;
        this.gson = new Gson();
    }

    // TODO: FALTA EL PAGINADO
    public List<Superhero> getAllSuperheroes(final Request request, final Response response) {
        final Filter filter = new Filter(request.queryParams(QUERY_PARAM_WORD_IN_NAME));

        response.status(HttpStatus.OK.value());
        return superheroService.getSuperheroes(filter);
    }

    public Superhero getSuperheroById(final Request request, final Response response) throws SuperheroException {
        final Long superheroId = getSuperheroId(request);

        try {
            response.status(HttpStatus.OK.value());
            return superheroService.getSuperheroById(superheroId);
        } catch (final SuperheroException e) {
            response.status(e.getStatus());
            throw e;
        }
    }

    public Superhero updateSuperhero(final Request request, final Response response) throws SuperheroException {
        final Long superheroId = getSuperheroId(request);

        try {
            response.status(HttpStatus.ACCEPTED.value());
            return superheroService.updateSuperhero(superheroId, this.gson.fromJson(request.body(), Superhero.class));
        } catch (final SuperheroException e) {
            response.status(e.getStatus());
            throw e;
        } catch (final JsonSyntaxException e) {
            response.status(HttpStatus.BAD_REQUEST.value());
            throw e;
        }
    }

    public void deleteSuperheroById(final Request request, final Response response) {
        final Long superheroId = getSuperheroId(request);

        superheroService.deleteSuperhero(superheroId);
        response.status(HttpStatus.OK.value());
    }

    private Long getSuperheroId(final Request request) {
        return StringUtils.isEmpty(request.params(PARAM_SUPERHERO_ID)) ? null : Long.parseLong(request.params(PARAM_SUPERHERO_ID));
    }

}
