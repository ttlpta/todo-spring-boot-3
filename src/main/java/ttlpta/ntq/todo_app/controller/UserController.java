package ttlpta.ntq.todo_app.controller;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ttlpta.ntq.todo_app.dto.ApiResponse;
import ttlpta.ntq.todo_app.dto.UserCreationRequest;
import ttlpta.ntq.todo_app.dto.UserPasswordUpdateRequest;
import ttlpta.ntq.todo_app.dto.UserUpdateRequest;
import ttlpta.ntq.todo_app.entity.User;
import ttlpta.ntq.todo_app.service.UserService;

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
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ApiResponse<List<User>> getAllUsers() {
        return ApiResponse.success(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') or (hasRole('USER') and #id == authentication.principal.id)")
    public ApiResponse<User> getUserById(@PathVariable UUID id) {
        return ApiResponse.success(userService.getUserById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ApiResponse<User> createUser(@Valid @RequestBody UserCreationRequest request) {
        return ApiResponse.success(userService.createUser(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or (hasRole('USER') and #id == authentication.principal.id)")
    public ApiResponse<User> updateUser(@PathVariable UUID id, @Valid @RequestBody UserUpdateRequest request) {
        return ApiResponse.success(userService.updateUser(id, request));
    }

    @PatchMapping("/{id}/password")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') or (hasRole('USER') and #id == authentication.principal.id)")
    public ApiResponse<User> updatePassword(@PathVariable UUID id, @Valid @RequestBody UserPasswordUpdateRequest request) {
        return ApiResponse.success(userService.updatePassword(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') and !@userService.isAdmin(#id)")
    public ApiResponse<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ApiResponse.success();
    }
}
