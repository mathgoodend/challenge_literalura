package com.mbonfim.literalura.repositories;

import com.mbonfim.literalura.models.Book;
import com.mbonfim.literalura.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Year;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT bo FROM Book bo WHERE LOWER(bo.title) = LOWER(:title)")
    List<Book> findByTitle(String title);

    @Query("SELECT au FROM Author au WHERE au.birthYear <= :year AND (au.deathYear IS NULL OR au.deathYear >= :ano)")
    List<Author> findLivingAuthors(@Param("year") Year year);

    @Query("SELECT au FROM Author au WHERE au.birthYear <= :year AND (au.deathYear IS NULL OR au.deathYear >= :ano)")
    List<Author> findLivingAuthorsRefined(@Param("year") Year year);

    @Query("SELECT au FROM Author au WHERE au.birthYear <= :year AND au.deathYear <= :year")
    List<Author> findAuthorByDeath(@Param("year") Year year);

    @Query("SELECT bo FROM Book bo WHERE bo.languages LIKE %:language%")
    List<Book> findByLanguage(@Param("language") String language);
}
