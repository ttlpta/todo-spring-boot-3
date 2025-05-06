package ttlpta.ntq.user.controller;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ttlpta.ntq.user.dto.ApiResponse;
import ttlpta.ntq.user.dto.UserCreationRequest;
import ttlpta.ntq.user.dto.UserPasswordUpdateRequest;
import ttlpta.ntq.user.dto.UserUpdateRequest;
import ttlpta.ntq.user.entity.User;
import ttlpta.ntq.user.service.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public ApiResponse<List<User>> getAllUsers() {
        return ApiResponse.success(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public ApiResponse<User> getUserById(@PathVariable UUID id) {
        return ApiResponse.success(userService.getUserById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ApiResponse<User> createUser(@Valid @RequestBody UserCreationRequest request) {
        return ApiResponse.success(userService.createUser(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ApiResponse<User> updateUser(@PathVariable UUID id, @Valid @RequestBody UserUpdateRequest request) {
        return ApiResponse.success(userService.updateUser(id, request));
    }

    @PatchMapping("/{id}/password")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public ApiResponse<User> updatePassword(@PathVariable UUID id, @Valid @RequestBody UserPasswordUpdateRequest request) {
        return ApiResponse.success(userService.updatePassword(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ApiResponse.success();
    }
} 
