package com.mindata.superheros.services;

import com.mindata.superheros.exceptions.SuperheroException;
import com.mindata.superheros.models.Superhero;
import com.mindata.superheros.repositories.SuperherosRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
public class SuperherosServiceImpl implements SuperherosService {

    private final SuperherosRepository superherosRepository;

    /**
     * Creates a SuperherosServiceImpl.
     *
     * @param superherosRepository  the SuperherosRepository.
     */
    public SuperherosServiceImpl(final SuperherosRepository superherosRepository) {
        this.superherosRepository = superherosRepository;
    }

    @Override
    public List<Superhero> getSuperheros() {
        return this.superherosRepository.findAll();
    }

    @Override
    public Superhero getSuperheroById(final Long superheroId)  throws SuperheroException {
        return this.superherosRepository.findById(superheroId).orElseThrow(() -> SuperheroException.ofNotFoundSuperhero());
    }

    @Override
    public List<Superhero> getSuperheroesWithWordInName(final String wordInName) {
        if (wordInName == null || wordInName.isBlank()) {
            return Collections.emptyList();
        }

        return this.superherosRepository.findWithWordInName(wordInName);
    }

    @Transactional
    @Override
    public Superhero updateSuperhero(final Long superheroId, final Superhero superheroWithUpdates) throws SuperheroException {
        if (superheroWithUpdates == null || superheroId == null
            || superheroWithUpdates.getName() == null || superheroWithUpdates.getName().isBlank()) {
            throw SuperheroException.ofBadRequest();
        }

        final Superhero savedSuperhero = getSuperheroById(superheroId);
        if (savedSuperhero.getName().equalsIgnoreCase(superheroWithUpdates.getName())) {
            return savedSuperhero;
        }

        savedSuperhero.setName(superheroWithUpdates.getName());

        return this.superherosRepository.saveAndFlush(savedSuperhero);
    }

    @Override
    public void deleteSuperhero(final Long superheroId) {
        this.superherosRepository.deleteById(superheroId);
    }
}
