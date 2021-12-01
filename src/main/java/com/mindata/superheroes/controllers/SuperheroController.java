package com.mindata.superheroes.controllers;

import com.mindata.superheroes.auth.Authorization;
import com.mindata.superheroes.auth.Permission;
import com.mindata.superheroes.entities.Filter;
import com.mindata.superheroes.exceptions.SuperheroException;
import com.mindata.superheroes.models.Superhero;
import com.mindata.superheroes.services.SuperheroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/superheroes")
public class SuperheroController {

    public static final String HEADER_TOKEN = "Token";

    private SuperheroService superheroService;

    @Autowired
    public SuperheroController(final SuperheroService superheroService) {
        this.superheroService = superheroService;
    }

    @Authorization(permission = Permission.VIEW)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Superhero>> getAllSuperheroes(
        @RequestHeader(HEADER_TOKEN) String token,
        @RequestParam(required = false, name = "name") final String name,
        @RequestParam final int page,
        @RequestParam final int size) {
        return new ResponseEntity<>(superheroService.getSuperheroes(new Filter(name), page, size), HttpStatus.OK);
    }

    @Authorization(permission = Permission.VIEW)
    @GetMapping(value = "/{superheroId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Superhero> getSuperheroById(@RequestHeader(HEADER_TOKEN) String token, @PathVariable final long superheroId)
        throws SuperheroException {
        return new ResponseEntity<>(superheroService.getSuperheroById(superheroId), HttpStatus.OK);
    }

    @Authorization(permission = Permission.UPDATE)
    @PutMapping(value = "/{superheroId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Superhero> updateSuperhero(@RequestHeader(HEADER_TOKEN) String token, @PathVariable final long superheroId,
        @RequestBody final Superhero superhero) throws SuperheroException {
        return new ResponseEntity<>(superheroService.updateSuperhero(superheroId, superhero), HttpStatus.OK);
    }

    @Authorization(permission = Permission.DELETE)
    @DeleteMapping(value = "/{superheroId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteSuperheroById(@RequestHeader(HEADER_TOKEN) String token, @PathVariable final long superheroId) {
        superheroService.deleteSuperhero(superheroId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
