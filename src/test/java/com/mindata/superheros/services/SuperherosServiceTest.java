package com.mindata.superheros.services;

import com.mindata.superheros.exceptions.SuperheroException;
import com.mindata.superheros.models.Superhero;
import com.mindata.superheros.repositories.SuperherosRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SuperherosServiceTest {

    private static final Long SUPERHERO_ID = 1L;
    private static final String SUPERHERO_NAME = "Superhero One";
    private static final Long OTHER_SUPERHERO_ID = 2L;
    private static final String OTHER_SUPERHERO_NAME = "Superhero Two";
    private static final String WORD_IN_NAME = "super";

    @Mock
    private SuperherosRepository superherosRepository;

    private SuperherosService superherosService;

    @BeforeTestClass
    public void setUpd() {
        superherosService = new SuperherosServiceImpl(superherosRepository);
    }

    @Test
    public void giveAllWhenGetSuperherosThenReturnAllSuperheros() {
        final List<Superhero> superheroes = superherosService.getSuperheros();

        assertNotNull(superheroes);
        assertFalse(superheroes.isEmpty());

        verify(superherosRepository, times(1)).findAll();
    }

    @Test
    public void giveIdWhenGetSuperheroByIdThenReturnSuperhero() {
        when(superherosRepository.findById(SUPERHERO_ID)).thenReturn(Optional.of(mock(Superhero.class)));

        final Superhero superhero = superherosService.getSuperheroById(SUPERHERO_ID);

        assertNotNull(superhero);

        verify(superherosRepository, times(1)).findById(SUPERHERO_ID);
    }

    @Test
    public void giveIncorrectIdWhenGetSuperheroByIdThenReturnBadRequest() {
        when(superherosRepository.findById(SUPERHERO_ID)).thenReturn(Optional.empty());

        final SuperheroException exception = assertThrows(SuperheroException.class,
            () -> superherosService.getSuperheroById(SUPERHERO_ID));

        assertEquals("superhero_not_found", exception.getError());
        assertEquals("Invalid or not found superhero", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

        verify(superherosRepository, times(1)).findById(SUPERHERO_ID);
    }

    @Test
    public void giveWordWhenGetSuperherosWithWordInNameThenReturnAllSuperherosWithWordInName() {
        final List<Superhero> superheroes = getMocksSuperheros();
        when(superherosRepository.findWithWordInName(WORD_IN_NAME)).thenReturn(superheroes);

        final List<Superhero> superheroesResult = superherosService.getSuperherosWithWordInName(WORD_IN_NAME);

        assertNotNull(superheroesResult);
        assertFalse(superheroesResult.isEmpty());
        assertEquals(superheroes.size(), superheroesResult.size());
        superheroes.forEach(superhero -> assertTrue(superheroesResult.contains(superhero)));
        superheroesResult.forEach(superhero -> {
            assertNotNull(superhero.getName());
            assertTrue(superhero.getName().contains(WORD_IN_NAME));
        });

        verify(superherosRepository, times(1)).findWithWordInName(WORD_IN_NAME);
    }

    @Test
    public void giveNewNameWhenUpdateSuperherosThenReturnUpdatedSuperhero() {
        final Superhero superheroWithNewName = mock(Superhero.class);
        when(superheroWithNewName.getName()).thenReturn("new name");

        final Superhero updatedSuperhero = mock(Superhero.class);
        when(updatedSuperhero.getId()).thenReturn(SUPERHERO_ID);
        when(updatedSuperhero.getName()).thenReturn("new name");

        when(superherosRepository.saveAndFlush(superheroWithNewName)).thenReturn(updatedSuperhero);

        final Superhero superhero = superherosService.updateSuperhero(SUPERHERO_ID, superheroWithNewName);

        assertNotNull(superhero);
        assertEquals(updatedSuperhero.getName(), superhero.getName());

        verify(superherosRepository, times(1)).saveAndFlush(superheroWithNewName);
    }

    @Test
    public void giveIdToDeleteWhenDeleteSuperheroByIdThenReturnSuccess() {
        superherosService.deleteSuperhero(SUPERHERO_ID);

        verify(superherosRepository, times(1)).deleteById(SUPERHERO_ID);
    }

    private List<Superhero> getMocksSuperheros() {
        final List<Superhero> superheroes = new ArrayList<>();

        final Superhero superheroOne = mock(Superhero.class);
        when(superheroOne.getId()).thenReturn(SUPERHERO_ID);
        when(superheroOne.getName()).thenReturn(SUPERHERO_NAME);
        superheroes.add(superheroOne);

        final Superhero superheroTwo = mock(Superhero.class);
        when(superheroTwo.getId()).thenReturn(OTHER_SUPERHERO_ID);
        when(superheroTwo.getName()).thenReturn(OTHER_SUPERHERO_NAME);
        superheroes.add(superheroTwo);

        return superheroes;
    }
}
