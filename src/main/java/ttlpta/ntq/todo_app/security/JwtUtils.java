package ttlpta.ntq.todo_app.security;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {
    private static final String BEARER_PREFIX = "Bearer ";

    public String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            return null;
        }
        return authorizationHeader.substring(BEARER_PREFIX.length());
    }

    public String extractTokenFromHeader(HttpHeaders headers) {
        String authorizationHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
        return extractTokenFromHeader(authorizationHeader);
    }
} 
