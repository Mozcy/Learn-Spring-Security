package sbs.getcry.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JWTUtils {

    private static final String SECRET_KEY = "sbs.getCry";        // 加密秘钥
    public static final long EXPIRATION_TIME = 60 * 60 * 1000L;   // 1小时过期时间

    /**
     * 生成 HMAC-SHA256 签名的 JWT Token
     *
     * @param claims 自定义 payload 数据
     * @return
     */
    public static String createToken(Map<String, Object> claims) {
        var currentDate = new Date();
        var expirationDate = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        return Jwts.builder()
                .setClaims(claims)                               // 设置自定义的数据
                .setIssuedAt(currentDate)                        // 设置签发时间
                .setExpiration(expirationDate)                   // 设置过期时间
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)  // 设置签名算法和秘钥
                .compact();
    }

    /**
     * 获取 Token 中 user_id
     *
     * @param token
     * @return
     */
    public static String getUserId(String token) throws ExpiredJwtException {
        var claims = parseToken(token);
        String userId = (String) claims.getOrDefault("user_id", "");
        return userId;
    }

    /**
     * 获取 Token 中 username
     *
     * @param token
     * @return
     */
    public static String getUserName(String token) throws ExpiredJwtException {
        var claims = parseToken(token);
        String username = (String) claims.getOrDefault("username", "");
        return username;
    }

    /**
     * 获取 Token 中 user_key 用户令牌
     *
     * @param token
     * @return
     */
    public static String getUserKey(String token) throws ExpiredJwtException {
        var claims = parseToken(token);
        String userKey = (String) claims.getOrDefault("user_key", "");
        return userKey;
    }


    /**
     * 获取 payload 用户数据
     *
     * @param token
     * @return Claims
     */
    public static Claims parseToken(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

}
