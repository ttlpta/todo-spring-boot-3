package ttlpta.ntq.todo_app.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtService {
    // For HS384, minimum key length is 384 bits (48 bytes)
    private static final String SECRET_KEY = "H9FhCXzCkMvct9TGJ3CgzNQDrhXZgULPzFyPUrXXxKDygDgkZVkP7xmZ3meE3khK";
    private static final long ACCESS_TOKEN_EXPIRATION = 86400000; // 24 hours
    private static final long REFRESH_TOKEN_EXPIRATION = 604800000; // 7 days

    public String generateAccessToken(String username) {
        return generateToken(username, ACCESS_TOKEN_EXPIRATION);
    }

    public String generateRefreshToken(String username) {
        return generateToken(username, REFRESH_TOKEN_EXPIRATION);
    }

    private String generateToken(String username, long expirationTime) {
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("username", username);

            // Create JWT claims set
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(username)
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + expirationTime))
                    .claim("username", username)
                    .jwtID(UUID.randomUUID().toString())
                    .build();

            // Create JWS header with HS384 algorithm
            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS384).build();

            // Create SignedJWT
            SignedJWT signedJWT = new SignedJWT(header, claimsSet);

            // Create signer with secret key
            JWSSigner signer = new MACSigner(SECRET_KEY);

            // Sign the JWT
            signedJWT.sign(signer);

            // Serialize to compact form
            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Error generating JWT token", e);
        }
    }
} 
