package korobkin.nikita.bookclub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewResponse {
    private String text;
    private Float rating;
}
