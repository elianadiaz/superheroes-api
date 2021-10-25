package com.mindata.superheros.repositories;

import com.mindata.superheros.models.Superhero;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SuperherosRepository extends JpaRepository<Superhero, Long> {
    List<Superhero> findWithWordInName(final String wordInName);
}
