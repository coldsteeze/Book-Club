package korobkin.nikita.bookclub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProfileDto {
    private String username;
    private Long quantityBooksRead;
    private int quantityReviewsWritten;
}
