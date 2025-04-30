package ttlpta.ntq.todo_app.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import ttlpta.ntq.todo_app.dto.UserCreationRequest;
import ttlpta.ntq.todo_app.dto.UserPasswordUpdateRequest;
import ttlpta.ntq.todo_app.dto.UserUpdateRequest;
import ttlpta.ntq.todo_app.entity.User;
import ttlpta.ntq.todo_app.exception.ApplicationException;
import ttlpta.ntq.todo_app.exception.UnauthorizedException;
import ttlpta.ntq.todo_app.repository.UserRepository;
import ttlpta.ntq.todo_app.security.Role;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public User createUser(UserCreationRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        if (request.getRole() != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
            if (!isAdmin && request.getRole() != Role.USER) {
                throw new UnauthorizedException("Only admins can create users with specific roles");
            }
            
            user.setRole(request.getRole());
        } else {
            user.setRole(Role.USER);
        }
        return userRepository.save(user);
    }

    public User updateUser(UUID id, UserUpdateRequest request) {
        User user = getUserById(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        boolean isManager = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MANAGER"));
        boolean isSameUser = authentication.getName().equals(user.getUsername());
        
        // Update basic user information
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        
        // Only admin can update role
        if (request.getRole() != null) {
            if (isAdmin) {
                user.setRole(request.getRole());
            } else if (isManager) {
                // Manager cannot update role
                throw new UnauthorizedException("Managers cannot update user roles");
            } else if (isSameUser) {
                // Regular user cannot update own role
                throw new UnauthorizedException("Users cannot update their own role");
            }
        }
        
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public User updatePassword(UUID id, UserPasswordUpdateRequest request) {
        User user = getUserById(id);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    public boolean isAdmin(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getRole() == Role.ADMIN;
    }
} 
