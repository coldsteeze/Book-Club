package korobkin.nikita.bookclub.service;

import jakarta.validation.Valid;
import korobkin.nikita.bookclub.dto.UserDto;
import korobkin.nikita.bookclub.dto.UserProfileDto;
import korobkin.nikita.bookclub.entity.User;
import korobkin.nikita.bookclub.entity.enums.BookStatus;
import korobkin.nikita.bookclub.entity.enums.UserRole;
import korobkin.nikita.bookclub.exception.PasswordException;
import korobkin.nikita.bookclub.exception.UserAlreadyExistsException;
import korobkin.nikita.bookclub.repository.ReviewRepository;
import korobkin.nikita.bookclub.repository.UserBookRepository;
import korobkin.nikita.bookclub.repository.UserRepository;
import korobkin.nikita.bookclub.security.JwtUtil;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserBookRepository userBookRepository;
    private final ReviewRepository reviewRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;

    public String register(@Valid UserDto userDto) {
        User user = convertUserDTOToUser(userDto);

        if (!userRepository.existsByUsername(user.getUsername())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole(UserRole.ROLE_USER);
            userRepository.save(user);
            return jwtUtil.generateToken(user.getUsername());
        } else {
            throw new UserAlreadyExistsException("User with this username already exists");
        }
    }

    private User convertUserDTOToUser(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    public UserProfileDto getUserProfile(User user) {
        UserProfileDto userProfileDTO = modelMapper.map(user, UserProfileDto.class);
        userProfileDTO.setQuantityBooksRead(userBookRepository.quantityBooksWithStatus(user.getId(), BookStatus.READ));
        userProfileDTO.setQuantityReviewsWritten(reviewRepository.countReviewsByUserId(user.getId()));
        return userProfileDTO;
    }

    public void updateProfileUsername(String username, User user) {
        if (!userRepository.existsByUsername(username)) {
            user.setUsername(username);
            userRepository.save(user);
        } else {
            throw new UserAlreadyExistsException("User with this username already exists");
        }
    }

    public void updateProfilePassword(String oldPassword, String newPassword, User user) {
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new PasswordException("Incorrect old password");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void deleteProfile(User user) {
        userRepository.deleteById(user.getId());
    }
}
