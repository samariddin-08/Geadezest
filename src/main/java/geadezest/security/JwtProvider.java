package geadezest.security;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtProvider {

    @Value("${token.key}")
    private String secretkey;

    @Value("${token.ttl}")
    private Long ttl;
    public String generateToken(String email) {

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ttl))
                .signWith(SignatureAlgorithm.HS256, secretkey)
                .compact();
    }
    public String gettokenForUsername(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretkey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            log.info("jwt muddati tugagan , {}", e.getMessage());
        } catch (CompressionException e) {
            log.info("jwt parse exception,{}", e.getMessage());
        } catch (JwtException e) {
            log.info("jwt  exception,{}", e.getMessage());
        }

        return null;
    }
    public boolean isValidToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretkey).parse(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.info("jwt muddati tugagan , {}", e.getMessage());
        } catch (CompressionException e) {
            log.info("jwt parse exception,{}", e.getMessage());
        } catch (JwtException e) {
            log.info("jwt  exception,{}", e.getMessage());
        }

        return false;
    }
}

