package com.mindata.superheroes.services;

import com.mindata.superheroes.exceptions.SuperheroException;
import com.mindata.superheroes.models.Superhero;
import com.mindata.superheroes.repositories.SuperherosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SuperherosServiceTest {

    private static final Long SUPERHERO_ID = 1L;
    private static final String SUPERHERO_NAME = "Superhero One";
    private static final String OTHER_SUPERHERO_NAME = "Superhero Two";
    private static final String WORD_IN_NAME = "super";

    @Mock
    private SuperherosRepository superherosRepository;

    private SuperherosService superherosService;

    @BeforeEach
    public void setUpd() {
        superherosService = new SuperherosServiceImpl(superherosRepository);
    }

    @Test
    public void giveAllWhenGetSuperherosThenReturnAllSuperheros() {
        final List<Superhero> superheroesMock = new ArrayList<>();
        superheroesMock.add(mock(Superhero.class));
        superheroesMock.add(mock(Superhero.class));
        when(superherosRepository.findAll()).thenReturn(superheroesMock);

        final List<Superhero> superheroes = superherosService.getSuperheros();

        assertNotNull(superheroes);
        assertFalse(superheroes.isEmpty());
        assertEquals(superheroesMock.size(), superheroes.size());

        verify(superherosRepository, times(1)).findAll();
    }

    @Test
    public void giveIdWhenGetSuperheroByIdThenReturnSuperhero() throws SuperheroException {
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
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatus());

        verify(superherosRepository, times(1)).findById(SUPERHERO_ID);
    }

    @Test
    public void giveWordWhenGetSuperheroesWithWordInNameThenReturnAllSuperherosWithWordInName() {
        final List<Superhero> superheroes = getMocksSuperheros();
        when(superherosRepository.findWithWordInName(WORD_IN_NAME)).thenReturn(superheroes);

        final List<Superhero> superheroesResult = superherosService.getSuperheroesWithWordInName(WORD_IN_NAME);

        assertNotNull(superheroesResult);
        assertFalse(superheroesResult.isEmpty());
        assertEquals(superheroes.size(), superheroesResult.size());
        superheroes.forEach(superhero -> assertTrue(superheroesResult.contains(superhero)));
        superheroesResult.forEach(superhero -> {
            assertNotNull(superhero.getName());
            assertThat(superhero.getName(), containsStringIgnoringCase(WORD_IN_NAME));
        });

        verify(superherosRepository, times(1)).findWithWordInName(WORD_IN_NAME);
    }

    @Test
    public void giveNewNameWhenUpdateSuperheroThenReturnUpdatedSuperhero() throws SuperheroException {
        final Superhero superheroWithNewName = mock(Superhero.class);
        when(superheroWithNewName.getName()).thenReturn("new name");

        final Superhero savedSuperhero = mock(Superhero.class);
        when(savedSuperhero.getName()).thenReturn(SUPERHERO_NAME);
        when(superherosRepository.findById(SUPERHERO_ID)).thenReturn(Optional.of(savedSuperhero));

        final Superhero updatedSuperhero = mock(Superhero.class);
        when(updatedSuperhero.getName()).thenReturn("new name");
        when(superherosRepository.saveAndFlush(any(Superhero.class))).thenReturn(updatedSuperhero);

        final Superhero superhero = superherosService.updateSuperhero(SUPERHERO_ID, superheroWithNewName);

        assertNotNull(superhero);
        assertEquals(updatedSuperhero.getName(), superhero.getName());

        verify(superherosRepository, times(1)).findById(SUPERHERO_ID);
        verify(superherosRepository, times(1)).saveAndFlush(any(Superhero.class));
    }

    @Test
    public void giveIdToDeleteWhenDeleteSuperheroByIdThenReturnSuccess() {
        superherosService.deleteSuperhero(SUPERHERO_ID);

        verify(superherosRepository, times(1)).deleteById(SUPERHERO_ID);
    }

    private List<Superhero> getMocksSuperheros() {
        final List<Superhero> superheroes = new ArrayList<>();

        final Superhero superheroOne = mock(Superhero.class);
        when(superheroOne.getName()).thenReturn(SUPERHERO_NAME);
        superheroes.add(superheroOne);

        final Superhero superheroTwo = mock(Superhero.class);
        when(superheroTwo.getName()).thenReturn(OTHER_SUPERHERO_NAME);
        superheroes.add(superheroTwo);

        return superheroes;
    }
}
