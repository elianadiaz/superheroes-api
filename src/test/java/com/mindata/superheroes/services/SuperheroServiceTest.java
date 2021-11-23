package com.mindata.superheroes.services;

import com.mindata.superheroes.exceptions.SuperheroException;
import com.mindata.superheroes.models.Superhero;
import com.mindata.superheroes.repositories.SuperheroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
public class SuperheroServiceTest {

    private static final Long SUPERHERO_ID = 1L;
    private static final String SUPERHERO_NAME = "Superhero One";
    private static final String OTHER_SUPERHERO_NAME = "Superhero Two";
    private static final String WORD_IN_NAME = "super";

    @Mock
    private SuperheroRepository superheroRepository;

    private SuperheroService superheroService;

    @BeforeEach
    public void setUpd() {
        superheroService = new SuperheroServiceImpl(superheroRepository);
    }

    @Test
    public void giveAllWhenGetSuperherosThenReturnAllSuperheros() {
        final List<Superhero> superheroesMock = new ArrayList<>();
        superheroesMock.add(mock(Superhero.class));
        superheroesMock.add(mock(Superhero.class));
        final PageRequest pageRequest = PageRequest.of(1, 4, Sort.by(Sort.Direction.ASC, "id"));
        when(superheroRepository.findAll(pageRequest)).thenReturn(new PageImpl<>(superheroesMock));

        final Page<Superhero> superheroes = superheroService.getSuperheroes(1, 4);

        assertNotNull(superheroes);
        assertFalse(superheroes.isEmpty());
        assertEquals(superheroesMock.size(), superheroes.toList().size());

        verify(superheroRepository, times(1)).findAll(pageRequest);
    }

    @Test
    public void giveIdWhenGetSuperheroByIdThenReturnSuperhero() throws SuperheroException {
        when(superheroRepository.findById(SUPERHERO_ID)).thenReturn(Optional.of(mock(Superhero.class)));

        final Superhero superhero = superheroService.getSuperheroById(SUPERHERO_ID);

        assertNotNull(superhero);

        verify(superheroRepository, times(1)).findById(SUPERHERO_ID);
    }

    @Test
    public void giveIncorrectIdWhenGetSuperheroByIdThenReturnBadRequest() {
        when(superheroRepository.findById(SUPERHERO_ID)).thenReturn(Optional.empty());

        final SuperheroException exception = assertThrows(SuperheroException.class,
            () -> superheroService.getSuperheroById(SUPERHERO_ID));

        assertEquals("superhero_not_found", exception.getError());
        assertEquals("Invalid or not found superhero", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

        verify(superheroRepository, times(1)).findById(SUPERHERO_ID);
    }

    @Test
    public void giveWordWhenGetSuperheroesWithWordInNameThenReturnAllSuperherosWithWordInName() {
        final Page<Superhero> superheroes = getMocksSuperheros();
        final PageRequest pageRequest = PageRequest.of(1, 4);
        when(superheroRepository.findWithWordInName(WORD_IN_NAME, pageRequest)).thenReturn(superheroes);

        final Page<Superhero> superheroesResult = superheroService.getSuperheroesWithWordInName(WORD_IN_NAME, 1, 4);

        assertNotNull(superheroesResult);
        assertFalse(superheroesResult.isEmpty());
        assertEquals(superheroes.toList().size(), superheroesResult.toList().size());
        superheroes.forEach(superhero -> assertTrue(superheroesResult.toList().contains(superhero)));
        superheroesResult.forEach(superhero -> {
            assertNotNull(superhero.getName());
            assertThat(superhero.getName(), containsStringIgnoringCase(WORD_IN_NAME));
        });

        verify(superheroRepository, times(1)).findWithWordInName(WORD_IN_NAME, pageRequest);
    }

    @Test
    public void giveNewNameWhenUpdateSuperheroThenReturnUpdatedSuperhero() throws SuperheroException {
        final Superhero superheroWithNewName = mock(Superhero.class);
        when(superheroWithNewName.getName()).thenReturn("new name");

        final Superhero savedSuperhero = mock(Superhero.class);
        when(savedSuperhero.getName()).thenReturn(SUPERHERO_NAME);
        when(superheroRepository.findById(SUPERHERO_ID)).thenReturn(Optional.of(savedSuperhero));

        final Superhero updatedSuperhero = mock(Superhero.class);
        when(updatedSuperhero.getName()).thenReturn("new name");
        when(superheroRepository.saveAndFlush(any(Superhero.class))).thenReturn(updatedSuperhero);

        final Superhero superhero = superheroService.updateSuperhero(SUPERHERO_ID, superheroWithNewName);

        assertNotNull(superhero);
        assertEquals(updatedSuperhero.getName(), superhero.getName());

        verify(superheroRepository, times(1)).findById(SUPERHERO_ID);
        verify(superheroRepository, times(1)).saveAndFlush(any(Superhero.class));
    }

    @Test
    public void giveIdToDeleteWhenDeleteSuperheroByIdThenReturnSuccess() {
        superheroService.deleteSuperhero(SUPERHERO_ID);

        verify(superheroRepository, times(1)).deleteById(SUPERHERO_ID);
    }

    private Page<Superhero> getMocksSuperheros() {
        final List<Superhero> superheroes = new ArrayList<>();

        final Superhero superheroOne = mock(Superhero.class);
        when(superheroOne.getName()).thenReturn(SUPERHERO_NAME);
        superheroes.add(superheroOne);

        final Superhero superheroTwo = mock(Superhero.class);
        when(superheroTwo.getName()).thenReturn(OTHER_SUPERHERO_NAME);
        superheroes.add(superheroTwo);

        return new PageImpl<>(superheroes);
    }
}
