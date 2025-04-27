package ttlpta.ntq.todo_app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ttlpta.ntq.todo_app.security.Role;
import ttlpta.ntq.todo_app.validation.UniqueUser;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationRequest {
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @UniqueUser(field = "email", message = "Email is already in use")
    private String email;
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @UniqueUser(field = "username", message = "Username is already taken")
    private String username;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    
    private Role role;  
} 
