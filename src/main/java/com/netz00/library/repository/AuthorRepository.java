package com.netz00.library.repository;

import com.netz00.library.domain.Author;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Author entity.
 */
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Query(
        value = "select distinct author from Author author left join fetch author.genres",
        countQuery = "select count(distinct author) from Author author"
    )
    Page<Author> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct author from Author author left join fetch author.genres")
    List<Author> findAllWithEagerRelationships();

    @Query("select author from Author author left join fetch author.genres where author.id =:id")
    Optional<Author> findOneWithEagerRelationships(@Param("id") Long id);
}
