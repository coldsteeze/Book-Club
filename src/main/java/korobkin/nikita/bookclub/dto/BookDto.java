package korobkin.nikita.bookclub.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import korobkin.nikita.bookclub.entity.enums.BookGenre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookDto {
    @NotBlank(message = "Title must not be blank")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @NotBlank(message = "Author must not be blank")
    @Size(max = 255, message = "Author must not exceed 255 characters")
    private String author;

    @Enumerated(EnumType.STRING)
    private BookGenre genre;

    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    private String isbn;
}
