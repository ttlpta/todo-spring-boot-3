package ttlpta.ntq.todo_app.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ttlpta.ntq.todo_app.dto.ApiResponse;
import ttlpta.ntq.todo_app.dto.LoginRequest;
import ttlpta.ntq.todo_app.service.AuthService;
import ttlpta.ntq.todo_app.service.TokenRevocationService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final TokenRevocationService tokenRevocationService;

    public AuthController(AuthService authService, TokenRevocationService tokenRevocationService) {
        this.authService = authService;
        this.tokenRevocationService = tokenRevocationService;
    }

    @PostMapping("/login")
    public ApiResponse<Map<String, String>> login(@Valid @RequestBody LoginRequest loginRequest) {
        Map<String, String> tokens = authService.login(loginRequest);
        return ApiResponse.success(tokens);
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestHeader("Authorization") String authorizationHeader) {
        String accessToken = authorizationHeader.replace("Bearer ", "");
        String refreshToken = ""; // You might want to get this from a different header or request body
        
        tokenRevocationService.revokeTokens(accessToken, refreshToken);
        return ApiResponse.success();
    }
} 
