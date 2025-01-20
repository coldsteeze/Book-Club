package korobkin.nikita.bookclub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Review")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "text")
    @Size(max = 1000, message = "Text cannot exceed 1000 characters")
    private String text;

    @Column(name = "rating", nullable = false)
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Float rating;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id", nullable = false)
    private Book book;

    @Column(name = "created_at")
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
