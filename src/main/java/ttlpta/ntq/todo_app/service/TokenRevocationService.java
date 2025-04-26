package ttlpta.ntq.todo_app.service;

import org.springframework.stereotype.Service;
import ttlpta.ntq.todo_app.entity.RevokeToken;
import ttlpta.ntq.todo_app.repository.RevokeTokenRepository;
import java.time.LocalDateTime;

@Service
public class TokenRevocationService {
    private final RevokeTokenRepository revokeTokenRepository;

    public TokenRevocationService(RevokeTokenRepository revokeTokenRepository) {
        this.revokeTokenRepository = revokeTokenRepository;
    }

    public void revokeTokens(String accessToken, String refreshToken) {
        RevokeToken revokeToken = new RevokeToken();
        revokeToken.setAccessToken(accessToken);
        revokeToken.setRefreshToken(refreshToken);
        revokeToken.setCreatedAt(LocalDateTime.now());
        revokeToken.setUpdatedAt(LocalDateTime.now());
        
        revokeTokenRepository.save(revokeToken);
    }

    public boolean isTokenRevoked(String token) {
        return revokeTokenRepository.existsByAccessToken(token) || revokeTokenRepository.existsByRefreshToken(token);
    }
} 
