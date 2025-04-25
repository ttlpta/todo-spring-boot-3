package ttlpta.ntq.todo_app.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ttlpta.ntq.todo_app.dto.ApiResponse;
import ttlpta.ntq.todo_app.dto.LoginRequest;
import ttlpta.ntq.todo_app.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(@Valid @RequestBody LoginRequest loginRequest) {
        String token = authService.login(loginRequest);
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);

        return ApiResponse.success(response);
    }
} 
