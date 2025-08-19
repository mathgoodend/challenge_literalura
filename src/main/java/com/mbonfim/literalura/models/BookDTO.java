package com.mbonfim.literalura.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BookDTO(@JsonAlias("title") String title,
                      @JsonAlias("download_count") Double downloadCount,
                      @JsonAlias("languages") List<String> language,
                      @JsonAlias("authors") List<AuthorDTO> authors) {
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Title: ").append(title).append("\n");
        sb.append("Author(s): \n");
        for (AuthorDTO author : authors) {
            sb.append("  - ").append(author.name()).append("\n");
        }
        sb.append("Language(s): ").append(String.join(", ", language)).append("\n");
        sb.append("Downloads: ").append(downloadCount).append("\n");
        sb.append("----------------------------------------");
        return sb.toString();
    }
}
