package korobkin.nikita.bookclub.service;

import korobkin.nikita.bookclub.dto.AuthenticationDto;
import korobkin.nikita.bookclub.exception.AuthenticationFailedException;
import korobkin.nikita.bookclub.security.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public String authenticate(AuthenticationDto authenticationDTO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(),
                        authenticationDTO.getPassword());

        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            throw new AuthenticationFailedException("Invalid username or password");
        }

        return jwtUtil.generateToken(authenticationToken.getPrincipal().toString());
    }
}
