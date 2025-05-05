package ttlpta.ntq.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "revoke_tokens")
public class RevokeToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    private String accessTokenJti;
    private String refreshTokenJti;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 
