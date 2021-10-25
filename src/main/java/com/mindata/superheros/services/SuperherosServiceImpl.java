package com.mindata.superheros.services;

import com.mindata.superheros.models.Superhero;
import com.mindata.superheros.repositories.SuperherosRepository;

import java.util.List;

public class SuperherosServiceImpl implements SuperherosService {

    public SuperherosServiceImpl(final SuperherosRepository superherosRepository) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Superhero> getSuperheros() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Superhero getSuperheroById(final Long superheroId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Superhero> getSuperherosWithWordInName(final String wordInName) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Superhero updateSuperhero(final Long superheroId, final Superhero superheroWithNewName) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void deleteSuperhero(final Long superheroId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
