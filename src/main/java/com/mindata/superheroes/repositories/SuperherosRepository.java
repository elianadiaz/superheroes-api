package com.mindata.superheroes.repositories;

import com.mindata.superheroes.models.Superhero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuperherosRepository extends JpaRepository<Superhero, Long> {

    /**
     * Find all superheroes with word in name.
     *
     * @param wordInName    the word in name to find.
     * @return superheroes's list.
     */
    @Query(value="select * from superhero s where UPPER(s.name) LIKE CONCAT('%',:wordInName,'%')", nativeQuery=true)
    List<Superhero> findWithWordInName(@Param("wordInName") final String wordInName);
}
