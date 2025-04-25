package ttlpta.ntq.todo_app.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;

import ttlpta.ntq.todo_app.constant.ErrorCode;
import ttlpta.ntq.todo_app.dto.LoginRequest;
import ttlpta.ntq.todo_app.entity.User;
import ttlpta.ntq.todo_app.exception.ApplicationException;
import ttlpta.ntq.todo_app.repository.UserRepository;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new ApplicationException("Invalid credentials", HttpStatus.UNAUTHORIZED, ErrorCode.AUTHENTICATION_ERROR));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new ApplicationException("Invalid credentials", HttpStatus.UNAUTHORIZED, ErrorCode.AUTHENTICATION_ERROR);
        }

        return jwtService.generateToken(user.getUsername());
    }
} 
