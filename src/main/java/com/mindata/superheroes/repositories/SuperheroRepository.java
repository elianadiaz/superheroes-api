package com.mindata.superheroes.repositories;

import com.mindata.superheroes.models.Superhero;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SuperheroRepository extends JpaRepository<Superhero, Long> {

    /**
     * Find all superheroes with word in name.
     *
     * @param wordInName    the word in name to find.
     * @return superheroes's list.
     */
    @Query(value="select * from superhero s where UPPER(s.name) LIKE UPPER(CONCAT('%',:wordInName,'%')) order by s.id",
        countQuery="select count(*) from superhero s where UPPER(s.name) LIKE UPPER(CONCAT('%',:wordInName,'%'))",
        nativeQuery=true)
    Page<Superhero> findWithWordInName(@Param("wordInName") final String wordInName, final Pageable pageable);
}
