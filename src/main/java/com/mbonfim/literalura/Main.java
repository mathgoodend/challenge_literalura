package com.mbonfim.literalura;

import com.mbonfim.literalura.models.*;
import com.mbonfim.literalura.repositories.*;
import com.mbonfim.literalura.services.*;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

import java.time.Year;

@Component
public class Main {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ConsumeAPI consumeAPI;

    @Autowired
    private JsonConversion jsonConversion;

    private final Scanner input = new Scanner(System.in);

    public Main(BookRepository bookRepository, ConsumeAPI consumeAPI, JsonConversion jsonConversion) {
        this.bookRepository = bookRepository;
        this.consumeAPI = consumeAPI;
        this.jsonConversion = jsonConversion;
    }

    public void execute() {
        boolean isRunning = true;
        while (isRunning) {
            renderMenu();
            Integer item = input.nextInt();

            switch(item) {
                case 1 -> findBooksByTitle();
                case 2 -> listAllBooks();
                case 3 -> listAllAuthors();
                case 4 -> listLivingAuthors();
                case 5 -> listLivingAuthorsRefined();
                case 6 -> listAuthorsByDeath();
                case 7 -> listBooksByLanguage();
                case 0 -> {
                    System.out.println("Thank you for using Literalura..");
                    isRunning = false;
                }
                default -> System.out.println("Invalid. Choose a valid item from the menu!");
            }
        }
    }

    public void renderMenu() {
        System.out.println("""
                ===================================================
                    Welcome to Literalura
                    - Choose one option from the menu below..
                -~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-
                 - [1] Search books by title
                 - [2] List all registered books
                 - [3] List all registered authors
                 - [4] Search living authors by year
                 - [5] Search dead authors by year
                 - [6] List authors ordered by date of death
                 - [7] List books filtering per language
                 - [0] Quit the application
                ===================================================
                """);
    }

    public void saveBooks(List<Book> books) {
        books.forEach(bookRepository::save);
    }



    private void findBooksByTitle() {
        String baseURL = "https://gutendex.com/books?search=";

        try {
            System.out.println("Book Title: ");
            String title = input.nextLine();
            String address = baseURL + title.replace(" ", "%20");
            String jsonResponse = consumeAPI.getData(address);

            if (jsonResponse.isEmpty()) {
                System.out.println("Empty response from the API");
                return;
            }

            JsonNode rootNode = jsonConversion.getMapper().readTree(jsonResponse);
            JsonNode resultsNode = rootNode.path("results");

            if (resultsNode.isEmpty()) {
                System.out.println("Book not found");
                return;
            }

            List<BookDTO> booksDTO = jsonConversion.getMapper()
                    .readerForListOf(BookDTO.class)
                    .readValue(resultsNode);

            List<Book> existingBooks = bookRepository.findByTitle(title);
            if (!existingBooks.isEmpty()) {
                for (Book existingBook : existingBooks) {
                    booksDTO.removeIf(bookDTO -> existingBook.geTitle().equals(bookDTO.title()));
                }
            }

            if (!booksDTO.isEmpty()) {
                List<Book> newBooks = booksDTO.stream().map(Book::new).collect(Collectors.toList());
                saveBooks(newBooks);
                System.out.println("Books successfully registered on database");
            } else {
                System.out.println("All books have already been registered to the database");
            }

            if (!booksDTO.isEmpty()) {
                System.out.println("Books found:");
                Set<String> shownTitles = new HashSet<>();
                for (BookDTO book : booksDTO) {
                    if (!shownTitles.contains(book.title())) {
                        System.out.println(book);
                        shownTitles.add(book.title());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error when looking for books: " + e.getMessage());
        }
    }

    private void listAllBooks() {
        List<Book> books = bookRepository.findAll();
        if (books.isEmpty()) {
            System.out.println("No book has been registered");
        } else {
            books.forEach(System.out::println);
        }
    }

    private void listAllAuthors() {
        List<Book> books = bookRepository.findAll();
        if (books.isEmpty()) {
            System.out.println("No books registered");
        } else {
            books.stream()
                    .map(Book::getAuthor)
                    .distinct()
                    .forEach(author -> System.out.println(author.getName()));
        }
    }

    private void listLivingAuthors() {
        System.out.println("Year: ");
        Integer inputYear = input.nextInt();
        input.nextLine();

        Year year = Year.of(inputYear);

        List<Author> authors = bookRepository.findLivingAuthors(year);
        if(authors.isEmpty()) {
            System.out.println("No living authors found");
        } else {
            System.out.println("%i living authors found for the year of %s:\n".formatted(authors.toArray().length, year.toString()));
            authors.forEach(author -> {
                    if(Author.hasYear(author.getBirthYear()) && Author.hasYear(author.getDeathYear())) {
                        String authorName = author.getName();
                        String birthYear = author.getBirthYear().toString();
                        String deathYear = author.getDeathYear().toString();
                        System.out.println("%s (%s - %s)".formatted(authorName, birthYear, deathYear));
                    }
            });
        }
    }

    private void listLivingAuthorsRefined() {
        System.out.println("Year: ");
        Integer inputYear = input.nextInt();
        input.nextLine();

        Year year = Year.of(inputYear);

        List<Author> authors = bookRepository.findLivingAuthorsRefined(year);
        if(authors.isEmpty()) {
            System.out.println("No living authors found");
        } else {
            System.out.println("%i living authors found for the year of %s:\n".formatted(authors.toArray().length, year.toString()));
            authors.forEach(author -> {
                if(Author.hasYear(author.getBirthYear()) && Author.hasYear(author.getDeathYear())) {
                    String authorName = author.getName();
                    String birthYear = author.getBirthYear().toString();
                    String deathYear = author.getDeathYear().toString();
                    System.out.println("%s (%s - %s)".formatted(authorName, birthYear, deathYear));
                }
            });
        }
    }

    private void listAuthorsByDeath() {
        System.out.println("Year: ");
        Integer inputYear = input.nextInt();
        input.nextLine();

        Year year = Year.of(inputYear);

        List<Author> authors = bookRepository.findAuthorByDeath(year);
        if(authors.isEmpty()) {
            System.out.println("No authors found");
        } else {
            System.out.println("%i authors found that died the year of %s:\n".formatted(authors.toArray().length, year.toString()));
            authors.forEach(author -> {
                if(Author.hasYear(author.getBirthYear()) && Author.hasYear(author.getDeathYear())) {
                    String authorName = author.getName();
                    String birthYear = author.getBirthYear().toString();
                    String deathYear = author.getDeathYear().toString();
                    System.out.println("%s (%s - %s)".formatted(authorName, birthYear, deathYear));
                }
            });
        }
    }

    private void listBooksByLanguage() {
        System.out.println("""
            Select your preferred language:
            English (en)
            Portuguese (pt)
            Spanish (es)
            French (fr)
            German (de)
            """);
        String lang = input.nextLine();

        List<Book> books = bookRepository.findByLanguage(lang);
        if (books.isEmpty()) {
            System.out.println("No book found with the specified language");
        } else {
            books.forEach(book -> {
                String title = book.geTitle();
                String author = book.getAuthor().getName();
                String bookLanguage = book.getLanguage();

                System.out.println("Title: %s\nAuthor: %s\nLanguage: %s".formatted(title, author, bookLanguage));
                System.out.println("----------------------------------------");
            });
        }
    }

}
