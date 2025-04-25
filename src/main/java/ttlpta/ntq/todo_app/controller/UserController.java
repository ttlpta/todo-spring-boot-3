package ttlpta.ntq.todo_app.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ttlpta.ntq.todo_app.dto.ApiResponse;
import ttlpta.ntq.todo_app.dto.UserCreationRequest;
import ttlpta.ntq.todo_app.dto.UserPasswordUpdateRequest;
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
    public ApiResponse<List<User>> getAllUsers() {
        return ApiResponse.success(userService.getAllUsers());
    }

    @PostMapping
    public ApiResponse<User> createUser(@Valid @RequestBody UserCreationRequest request) {
        return ApiResponse.success(userService.createUser(request));
    }

    @GetMapping("/{id}")
    public ApiResponse<User> getUserById(@PathVariable UUID id) {
        return ApiResponse.success(userService.getUserById(id));
    }

    @PatchMapping("/{id}/password")
    public ApiResponse<User> updatePassword(@PathVariable UUID id, @Valid @RequestBody UserPasswordUpdateRequest request) {
        return ApiResponse.success(userService.updatePassword(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ApiResponse.success();
    }
}
