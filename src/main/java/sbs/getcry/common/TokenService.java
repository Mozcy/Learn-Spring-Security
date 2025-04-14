package sbs.getcry.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import sbs.getcry.bo.LoginUserDetails;
import sbs.getcry.utils.JWTUtils;

import java.util.HashMap;
import java.util.UUID;

/**
 * @Author : saeko
 * @Date: 2025/4/7 14:52
 * @Description: 用于操作 token 到 Redis 的工具类
 */

@Component
public class TokenService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 登录成功后, 生成 token 并存储到 Redis 中
     *
     * @param loginUserDetails
     * @return
     */
    public String createToken(LoginUserDetails loginUserDetails) {
        var claims = new HashMap<String, Object>();
        var uuid = UUID.randomUUID().toString();
        claims.put("userId", loginUserDetails.getSysUser().getUserId());
        claims.put("username", loginUserDetails.getSysUser().getUserName());
        claims.put("user_key", uuid);
        var token = JWTUtils.createToken(claims);
        redisTemplate.opsForValue().set("token:" + uuid, loginUserDetails);
        return token;
    }

    /**
     * 从 Redis 中获取登录用户信息
     *
     * @param userKey
     * @return
     */
    public LoginUserDetails getLoginUser(String userKey) {
        return (LoginUserDetails) redisTemplate.opsForValue().get("token:" + userKey);
    }

    /**
     * 删除 Redis 中的登录用户信息
     *
     * @param userKey
     */
    public void deleteLoginUser(String userKey) {
        redisTemplate.delete("token:" + userKey);
    }
}
