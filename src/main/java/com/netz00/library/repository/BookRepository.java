package com.netz00.library.repository;

import com.netz00.library.domain.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Book entity.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query(
        value = "select distinct book from Book book left join fetch book.genres",
        countQuery = "select count(distinct book) from Book book"
    )
    Page<Book> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct book from Book book left join fetch book.genres")
    List<Book> findAllWithEagerRelationships();

    @Query("select book from Book book left join fetch book.genres where book.id =:id")
    Optional<Book> findOneWithEagerRelationships(@Param("id") Long id);
}
