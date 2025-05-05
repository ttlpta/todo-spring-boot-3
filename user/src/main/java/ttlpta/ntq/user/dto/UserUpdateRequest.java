package ttlpta.ntq.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ttlpta.ntq.user.security.Role;

@Data
public class UserUpdateRequest {
    @NotBlank(message = "Username cannot be blank")
    private String username;
    
    private String password;
    
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;
    
    private Role role;
} 
