package com.mbonfim.literalura.models;

import jakarta.persistence.*;

import java.time.Year;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "birth_year")
    private Year birthYear;

    @Column(name = "death_year")
    private Year deathYear;

    @OneToMany(mappedBy = "name", fetch = FetchType.EAGER)
    private List<Book> books = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Year getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Year birthYear) {
        this.birthYear = birthYear;
    }

    public Year getDeathYear() {
        return deathYear;
    }

    public void setDeathYear(Year deathYear) {
        this.deathYear = deathYear;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public Author() {}

    public static boolean hasYear(Year year) {
        return year != null && !year.equals(Year.of(0));
    }

    public Author(AuthorDTO authorDTO) {
        this.name = authorDTO.name();
        this.birthYear = authorDTO.birthYear() != null ? Year.of(authorDTO.birthYear()) : null;
        this.deathYear = authorDTO.deathYear() != null ? Year.of(authorDTO.deathYear()) : null;
    }

    public Author(String name, Year birthYear, Year deathYear) {
        this.name = name;
        this.birthYear = birthYear;
        this.deathYear = deathYear;
    }

    @Override
    public String toString() {
        String birthYearString = birthYear != null ? birthYear.toString() : "Unknown";
        String deathYearString = deathYear != null ? deathYear.toString() : "Unknown";

        return "Author: %s (born %s, deceased %s)".formatted(name, birthYear, deathYear);
    }
}
