package ttlpta.ntq.todo_app.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;

import ttlpta.ntq.todo_app.constant.ErrorCode;
import ttlpta.ntq.todo_app.dto.LoginRequest;
import ttlpta.ntq.todo_app.entity.User;
import ttlpta.ntq.todo_app.exception.ApplicationException;
import ttlpta.ntq.todo_app.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

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

    public Map<String, String> login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new ApplicationException("Invalid credentials", HttpStatus.UNAUTHORIZED, ErrorCode.AUTHENTICATION_ERROR));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new ApplicationException("Invalid credentials", HttpStatus.UNAUTHORIZED, ErrorCode.AUTHENTICATION_ERROR);
        }

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", jwtService.generateAccessToken(user.getUsername()));
        tokens.put("refreshToken", jwtService.generateRefreshToken(user.getUsername()));

        return tokens;
    }

    public Map<String, String> refreshToken(String oldAccessToken, String refreshToken) {
        // Validate refresh token
        if (!jwtService.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        // Get username from refresh token
        String username = jwtService.extractUsername(refreshToken);
        
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", jwtService.generateAccessToken(username));
        tokens.put("refreshToken", jwtService.generateRefreshToken(username));

        return tokens;
    }
} 
