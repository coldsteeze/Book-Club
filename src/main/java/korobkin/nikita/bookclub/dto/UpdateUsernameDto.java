package korobkin.nikita.bookclub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUsernameDto {
    @NotBlank(message = "Username must not be blank")
    @Size(min = 3, max = 15, message = "Username must be between 3 and 15 characters long")
    private String username;
}
