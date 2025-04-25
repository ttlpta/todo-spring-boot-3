package ttlpta.ntq.todo_app.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;

// import ttlpta.ntq.todo_app.config.PasswordEncoderConfig;
import ttlpta.ntq.todo_app.dto.UserCreationRequest;
import ttlpta.ntq.todo_app.dto.UserPasswordUpdateRequest;
import ttlpta.ntq.todo_app.entity.User;
import ttlpta.ntq.todo_app.exception.ApplicationException;
import ttlpta.ntq.todo_app.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    // private final PasswordEncoderConfig passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
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
            throw new ApplicationException("User not found", HttpStatus.NOT_FOUND, "1001");
        }
        userRepository.deleteById(id);
    }
} 
