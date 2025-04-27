package ttlpta.ntq.todo_app.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ttlpta.ntq.todo_app.entity.User;
import ttlpta.ntq.todo_app.repository.UserRepository;
import ttlpta.ntq.todo_app.security.Role;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Check if super admin already exists
        if (userRepository.findByUsername("superadmin").isEmpty()) {
            User superAdmin = User.builder()
                    .username("superadmin")
                    .password(passwordEncoder.encode("superadmin"))
                    .email("ttlpta@gmail.com")
                    .role(Role.ADMIN)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            userRepository.save(superAdmin);
            System.out.println("Super admin user created successfully!");
        }
    }
} 
