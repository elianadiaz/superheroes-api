package com.mindata.superheros.services;

import com.mindata.superheros.models.Superhero;

import java.util.List;

public interface SuperherosService {
    List<Superhero> getSuperheros();

    Superhero getSuperheroById(final Long superheroId);

    List<Superhero> getSuperherosWithWordInName(final String wordInName);

    Superhero updateSuperhero(final Long superheroId, final Superhero superheroWithNewName);

    void deleteSuperhero(final Long superheroId);
}
