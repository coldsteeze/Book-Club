package korobkin.nikita.bookclub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import korobkin.nikita.bookclub.entity.enums.BookGenre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "title", nullable = false)
    @NotBlank(message = "Title must not be blank")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @Column(name = "author", nullable = false)
    @NotBlank(message = "Author must not be blank")
    @Size(max = 255, message = "Author must not exceed 255 characters")
    private String author;

    @Column(name = "genre", nullable = false)
    @Enumerated(EnumType.STRING)
    private BookGenre genre;

    @Column(name = "description", length = 2000)
    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    @Column(name = "ISBN", unique = true)
    private String isbn;

    @Column(name = "rating")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Float rating;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserBook> userBooks = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    private void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    private void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
