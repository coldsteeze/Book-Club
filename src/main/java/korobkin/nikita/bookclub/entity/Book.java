package korobkin.nikita.bookclub.entity;

import jakarta.persistence.*;
import korobkin.nikita.bookclub.entity.enums.BookGenre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Book")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "genre")
    private BookGenre genre;

    @Column(name = "description")
    private String description;

    @Column(name = "ISBN")
    private String isbn;

    @Column(name = "rating")
    private float rating;
}
