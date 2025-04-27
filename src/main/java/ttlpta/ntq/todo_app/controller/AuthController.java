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
import ttlpta.ntq.todo_app.dto.RefreshTokenRequest;
import ttlpta.ntq.todo_app.security.JwtUtils;
import ttlpta.ntq.todo_app.service.AuthService;
import ttlpta.ntq.todo_app.service.TokenRevocationService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final TokenRevocationService tokenRevocationService;
    private final JwtUtils jwtUtils;

    public AuthController(AuthService authService, 
                         TokenRevocationService tokenRevocationService,
                         JwtUtils jwtUtils) {
        this.authService = authService;
        this.tokenRevocationService = tokenRevocationService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ApiResponse<Map<String, String>> login(@Valid @RequestBody LoginRequest loginRequest) {
        Map<String, String> tokens = authService.login(loginRequest);
        return ApiResponse.success(tokens);
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestHeader("Authorization") String authorizationHeader) {
        String accessToken = jwtUtils.extractTokenFromHeader(authorizationHeader);
        if (accessToken == null) {
            return ApiResponse.error("Invalid authorization header");
        }
        
        tokenRevocationService.revokeTokens(accessToken, "");
        return ApiResponse.success();
    }

    @PostMapping("/refresh-token")
    public ApiResponse<Map<String, String>> refreshToken(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        
        String oldAccessToken = jwtUtils.extractTokenFromHeader(authorizationHeader);
        if (oldAccessToken == null) {
            return ApiResponse.error("Invalid authorization header");
        }
        
        String refreshToken = refreshTokenRequest.getRefreshToken();
        
        // Revoke old tokens
        tokenRevocationService.revokeTokens(oldAccessToken, refreshToken);
        
        // Generate new tokens
        Map<String, String> newTokens = authService.refreshToken(oldAccessToken, refreshToken);
        return ApiResponse.success(newTokens);
    }
} 
