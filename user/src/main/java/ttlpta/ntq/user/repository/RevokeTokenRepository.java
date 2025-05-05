package ttlpta.ntq.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ttlpta.ntq.user.entity.RevokeToken;

public interface RevokeTokenRepository extends JpaRepository<RevokeToken, Long> {
    boolean existsByUsernameAndAccessTokenJti(String username, String accessTokenJti);
    boolean existsByUsernameAndRefreshTokenJti(String username, String refreshTokenJti);
} 
