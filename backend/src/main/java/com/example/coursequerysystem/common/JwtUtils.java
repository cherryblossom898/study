package com.example.coursequerysystem.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtils {

    // 密钥（生产环境应放在配置文件）
    private static final String SECRET_KEY = "YourSecretKeyForJWTGenerationMustBeAtLeast256BitsLong!";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 2; // 2小时

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成 JWT Token
     */
    public String generateToken(LoginUser user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getUserId())
                .claim("role", user.getRole())
                .claim("relateId", user.getRelateId())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 从 Token 中解析用户信息
     */
    public LoginUser parseToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        LoginUser user = new LoginUser();
        user.setUserId(claims.get("userId", String.class));
        user.setUsername(claims.getSubject());
        user.setRole(claims.get("role", String.class));
        user.setRelateId(claims.get("relateId", String.class));
        return user;
    }

    /**
     * 验证 Token 是否有效（签名和过期）
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}