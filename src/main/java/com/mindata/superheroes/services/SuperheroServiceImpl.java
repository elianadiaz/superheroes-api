package com.mindata.superheroes.services;

import com.mindata.superheroes.entities.Filter;
import com.mindata.superheroes.exceptions.SuperheroException;
import com.mindata.superheroes.models.Superhero;
import com.mindata.superheroes.repositories.SuperheroRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
public class SuperheroServiceImpl implements SuperheroService {

    private final SuperheroRepository superheroRepository;

    /**
     * Creates a SuperherosServiceImpl.
     *
     * @param superheroRepository  the SuperherosRepository.
     */
    public SuperheroServiceImpl(@Autowired final SuperheroRepository superheroRepository) {
        this.superheroRepository = superheroRepository;
    }

    @Override
    public List<Superhero> getSuperheroes(final Filter filter) {
        if (filter != null && filter.getWordInName() != null && !filter.getWordInName().isBlank()) {
            return getSuperheroesWithWordInName(filter.getWordInName());
        }

        return getSuperheroes();
    }

    @Override
    public List<Superhero> getSuperheroes() {
        return this.superheroRepository.findAll();
    }

    @Override
    public Superhero getSuperheroById(final Long superheroId)  throws SuperheroException {
        return this.superheroRepository.findById(superheroId).orElseThrow(() -> SuperheroException.ofNotFoundSuperhero());
    }

    @Override
    public List<Superhero> getSuperheroesWithWordInName(final String wordInName) {
        if (wordInName == null || wordInName.trim().isBlank()) {
            return Collections.emptyList();
        }

        return this.superheroRepository.findWithWordInName(wordInName.trim());
    }

    @Transactional
    @Override
    public Superhero updateSuperhero(final Long superheroId, final Superhero superheroWithUpdates) throws SuperheroException {
        if (superheroWithUpdates == null || superheroId == null
            || superheroWithUpdates.getName() == null || superheroWithUpdates.getName().trim().isBlank()) {
            throw SuperheroException.ofBadRequest();
        }

        final Superhero savedSuperhero = getSuperheroById(superheroId);
        if (savedSuperhero.getName().equalsIgnoreCase(superheroWithUpdates.getName().trim())) {
            return savedSuperhero;
        }

        savedSuperhero.setName(superheroWithUpdates.getName().trim());

        return this.superheroRepository.saveAndFlush(savedSuperhero);
    }

    @Override
    public void deleteSuperhero(final Long superheroId) {
        this.superheroRepository.deleteById(superheroId);
    }
}
