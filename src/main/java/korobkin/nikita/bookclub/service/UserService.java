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
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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
        if (userRepository.existsByUsername(userDto.getUsername())) {
            log.warn("Attempt to register with existing username: {}", userDto.getUsername());
            throw new UserAlreadyExistsException("User with this username already exists");
        }

        User user = convertUserDtoToUser(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.ROLE_USER);
        userRepository.save(user);

        log.info("User '{}' registered successfully", user.getUsername());
        return jwtUtil.generateToken(user.getUsername());
    }

    private User convertUserDtoToUser(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    public UserProfileDto getUserProfile(User user) {
        log.info("Fetching profile for user '{}'", user.getUsername());
        UserProfileDto userProfileDTO = modelMapper.map(user, UserProfileDto.class);
        userProfileDTO.setQuantityBooksRead(userBookRepository.quantityBooksWithStatus(user.getId(), BookStatus.READ));
        userProfileDTO.setQuantityReviewsWritten(reviewRepository.countReviewsByUserId(user.getId()));
        return userProfileDTO;
    }

    public void updateProfileUsername(String username, User user) {
        log.info("User '{}' is updating their username to '{}'", user.getUsername(), username);
        if (!userRepository.existsByUsername(username)) {
            user.setUsername(username);
            userRepository.save(user);
            log.info("Username for user '{}' updated to '{}'", user.getUsername(), username);
        } else {
            log.warn("Attempt to update username to existing username: {}", username);
            throw new UserAlreadyExistsException("User with this username already exists");
        }
    }

    public void updateProfilePassword(String oldPassword, String newPassword, User user) {
        log.info("User '{}' is updating their password", user.getUsername());
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            log.warn("Incorrect old password for user '{}'", user.getUsername());
            throw new PasswordException("Incorrect old password");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Password updated successfully for user '{}'", user.getUsername());
    }

    public void deleteProfile(User user) {
        log.info("User '{}' is deleting their profile", user.getUsername());
        userRepository.deleteById(user.getId());
        log.info("Profile for user '{}' deleted successfully", user.getUsername());
    }
}

