package com.mbonfim.literalura.models;

import com.fasterxml.jackson.annotation.JsonAlias;

public record AuthorDTO(@JsonAlias("name") String name,
                        @JsonAlias("birth_year") Integer birthYear,
                        @JsonAlias("death_year") Integer deathYear) {
    @Override
    public String toString() {
        return "Author: " + name;
    }
}
