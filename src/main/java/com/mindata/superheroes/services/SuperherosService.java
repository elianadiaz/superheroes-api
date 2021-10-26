package com.mindata.superheroes.services;

import com.mindata.superheroes.exceptions.SuperheroException;
import com.mindata.superheroes.models.Superhero;

import java.util.List;

public interface SuperherosService {

    /**
     * Get all superheroes.
     *
     * @return superheroes's list.
     */
    List<Superhero> getSuperheros();

    /**
     * Get superhero by id.
     *
     * @param superheroId   the superhero id.
     * @return a {@link Superhero}.
     * @throws {@link SuperheroException} when not found a Superhero with this id.
     */
    Superhero getSuperheroById(final Long superheroId) throws SuperheroException;

    /**
     * Get all all the superheroes that contain that word in their name.
     *
     * @param wordInName    the word to find in name.
     * @return superheroes's list.
     */
    List<Superhero> getSuperheroesWithWordInName(final String wordInName);

    /**
     * Update superhero.
     *
     * @param superheroId           the superhero id.
     * @param superheroWithUpdates  the superhero with updates.
     * @return updated superhero.
     * @throws {@link SuperheroException} when not found a Superhero with this id.
     */
    Superhero updateSuperhero(final Long superheroId, final Superhero superheroWithUpdates) throws SuperheroException;

    /**
     * Delete superhero by id.
     *
     * @param superheroId           the superhero id.
     */
    void deleteSuperhero(final Long superheroId);
}
