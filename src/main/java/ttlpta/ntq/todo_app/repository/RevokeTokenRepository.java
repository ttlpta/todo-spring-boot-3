package ttlpta.ntq.todo_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ttlpta.ntq.todo_app.entity.RevokeToken;
import java.util.Optional;
import java.util.UUID;

public interface RevokeTokenRepository extends JpaRepository<RevokeToken, UUID> {
    Optional<RevokeToken> findByAccessToken(String accessToken);
    Optional<RevokeToken> findByRefreshToken(String refreshToken);
    boolean existsByAccessToken(String accessToken);
    boolean existsByRefreshToken(String refreshToken);
} 
