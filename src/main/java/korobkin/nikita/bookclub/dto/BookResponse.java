package korobkin.nikita.bookclub.dto;

import korobkin.nikita.bookclub.entity.enums.BookGenre;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BookResponse {
    private String title;

    private String author;

    private BookGenre genre;

    private String description;

    private String isbn;

    private List<ReviewResponse> reviews;
}
