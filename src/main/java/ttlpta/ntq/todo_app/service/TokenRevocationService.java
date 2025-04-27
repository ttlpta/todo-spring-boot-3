package ttlpta.ntq.todo_app.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ttlpta.ntq.todo_app.entity.RevokeToken;
import ttlpta.ntq.todo_app.repository.RevokeTokenRepository;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.Map;

@Service
public class TokenRevocationService {
    private final RevokeTokenRepository revokeTokenRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtService jwtService;
    private static final String ACCESS_TOKEN_PREFIX = "access_token:";
    private static final String REFRESH_TOKEN_PREFIX = "refresh_token:";
    private static final long TOKEN_EXPIRATION_DAYS = 30;

    public TokenRevocationService(RevokeTokenRepository revokeTokenRepository, 
                                RedisTemplate<String, String> redisTemplate,
                                JwtService jwtService) {
        this.revokeTokenRepository = revokeTokenRepository;
        this.redisTemplate = redisTemplate;
        this.jwtService = jwtService;
    }

    public void revokeTokens(String accessToken, String refreshToken) {
        Map<String, String> accessTokenInfo = jwtService.extractTokenInfo(accessToken);
        Map<String, String> refreshTokenInfo = jwtService.extractTokenInfo(refreshToken);

        String username = accessTokenInfo.get("username");
        String accessTokenJti = accessTokenInfo.get("jti");
        String refreshTokenJti = refreshTokenInfo.get("jti");

        // Store in Redis with expiration
        redisTemplate.opsForValue().set(
            ACCESS_TOKEN_PREFIX + username + ":" + accessTokenJti, 
            "revoked", 
            TOKEN_EXPIRATION_DAYS, 
            TimeUnit.DAYS
        );
        redisTemplate.opsForValue().set(
            REFRESH_TOKEN_PREFIX + username + ":" + refreshTokenJti, 
            "revoked", 
            TOKEN_EXPIRATION_DAYS, 
            TimeUnit.DAYS
        );

        // Also store in database as backup
        RevokeToken revokeToken = new RevokeToken();
        revokeToken.setUsername(username);
        revokeToken.setAccessTokenJti(accessTokenJti);
        revokeToken.setRefreshTokenJti(refreshTokenJti);
        revokeToken.setCreatedAt(LocalDateTime.now());
        revokeToken.setUpdatedAt(LocalDateTime.now());
        
        revokeTokenRepository.save(revokeToken);
    }

    public boolean isTokenRevoked(String token) {
        Map<String, String> tokenInfo = jwtService.extractTokenInfo(token);
        String username = tokenInfo.get("username");
        String jti = tokenInfo.get("jti");

        // First check Redis
        Boolean isAccessTokenRevoked = redisTemplate.hasKey(ACCESS_TOKEN_PREFIX + username + ":" + jti);
        Boolean isRefreshTokenRevoked = redisTemplate.hasKey(REFRESH_TOKEN_PREFIX + username + ":" + jti);
        
        if (Boolean.TRUE.equals(isAccessTokenRevoked) || Boolean.TRUE.equals(isRefreshTokenRevoked)) {
            return true;
        }

        // If not found in Redis, check database
        return revokeTokenRepository.existsByUsernameAndAccessTokenJti(username, jti) || 
               revokeTokenRepository.existsByUsernameAndRefreshTokenJti(username, jti);
    }
} 
