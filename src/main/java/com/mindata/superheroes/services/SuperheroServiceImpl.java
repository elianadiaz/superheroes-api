package com.mindata.superheroes.services;

import com.mindata.superheroes.entities.Filter;
import com.mindata.superheroes.exceptions.SuperheroException;
import com.mindata.superheroes.models.Superhero;
import com.mindata.superheroes.repositories.SuperheroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
public class SuperheroServiceImpl implements SuperheroService {

    private static final String NAME_CACHE = "superheroes";

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
    @Cacheable(NAME_CACHE)
    public List<Superhero> getSuperheroes() {
        return this.superheroRepository.findAll();
    }

    @Override
    @Cacheable(NAME_CACHE)
    public Superhero getSuperheroById(final Long superheroId)  throws SuperheroException {
        return this.superheroRepository.findById(superheroId).orElseThrow(() -> SuperheroException.ofNotFoundSuperhero());
    }

    @Override
    @Cacheable(NAME_CACHE)
    public List<Superhero> getSuperheroesWithWordInName(final String wordInName) {
        if (wordInName == null || wordInName.trim().isBlank()) {
            return Collections.emptyList();
        }

        return this.superheroRepository.findWithWordInName(wordInName.trim());
    }

    @Transactional
    @Override
    @CachePut(NAME_CACHE)
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
    @CacheEvict(NAME_CACHE)
    public void deleteSuperhero(final Long superheroId) {
        this.superheroRepository.deleteById(superheroId);
    }
}
