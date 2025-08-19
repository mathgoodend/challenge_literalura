package com.mbonfim.literalura.models;

import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String title;

    @ManyToOne(cascade = CascadeType.ALL)
    private Author author;

    private String language;

    private Integer authorBirthYear;

    private Integer authorDeathYear;

    private Double downloadCount;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String geTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getAuthorBirthYear() {
        return authorBirthYear;
    }

    public void setAuthorBirthYear(Integer authorBirthYear) {
        this.authorBirthYear = authorBirthYear;
    }

    public Integer getAuthorDeathYear() {
        return authorDeathYear;
    }

    public void setAuthorDeathYear(Integer authorDeathYear) {
        this.authorDeathYear = authorDeathYear;
    }

    public Double getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Double downloadCount) {
        this.downloadCount = downloadCount;
    }

    // Construtores
    public Book() {}

    public Book(BookDTO bookDTO) {
        this.title = bookDTO.title();
        Author author = new Author(bookDTO.authors().get(0));
        this.author = author;
        this.language = bookDTO.langauges().get(0);
        this.downloadCount = bookDTO.downloadCount();
    }

    public Book(Long idApi, String title, Author author, String language, Double downloadCount) {
        this.title = title;
        this.author = author;
        this.language = language;
        this.downloadCount = downloadCount;
    }

    @Override
    public String toString() {
        return "Title: " + title + "\n" +
                "Author: " + author.getName() + "\n" +
                "Language: " + language + "\n" +
                "Downloads: " + downloadCount + "\n" +
                "----------------------------------------";
    }
}
