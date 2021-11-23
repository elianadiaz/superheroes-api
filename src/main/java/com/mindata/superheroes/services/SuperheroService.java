package com.mindata.superheroes.services;

import com.mindata.superheroes.entities.Filter;
import com.mindata.superheroes.exceptions.SuperheroException;
import com.mindata.superheroes.models.Superhero;
import org.springframework.data.domain.Page;

public interface SuperheroService {

    /**
     * Get all superheroes.
     *
     * @param filter    filters to get superheroes.
     * @param page      the page.
     * @param size      the page size.
     * @return Paginated listing of Superheroes.
     */
    Page<Superhero> getSuperheroes(final Filter filter, int page, int size);

    /**
     * Get all superheroes.
     *
     * @param page      the page.
     * @param size      the page size.
     * @return Paginated listing of Superheroes.
     */
    Page<Superhero> getSuperheroes(int page, int size);

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
     * @param page          the page.
     * @param size          the page size.
     * @return Paginated listing of Superheroes.
     */
    Page<Superhero> getSuperheroesWithWordInName(final String wordInName, int page, int size);

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
