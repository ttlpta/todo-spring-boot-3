package ttlpta.ntq.todo_app.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
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

    public boolean validateToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(SECRET_KEY);
            
            // Verify signature and check expiration
            return signedJWT.verify(verifier) && 
                   !signedJWT.getJWTClaimsSet().getExpirationTime().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Error extracting username from token", e);
        }
    }

    public String extractJti(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getJWTID();
        } catch (Exception e) {
            throw new RuntimeException("Error extracting jti from token", e);
        }
    }

    public Map<String, String> extractTokenInfo(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            Map<String, String> tokenInfo = new HashMap<>();
            tokenInfo.put("username", claims.getSubject());
            tokenInfo.put("jti", claims.getJWTID());
            return tokenInfo;
        } catch (Exception e) {
            throw new RuntimeException("Error extracting token info", e);
        }
    }
} 
