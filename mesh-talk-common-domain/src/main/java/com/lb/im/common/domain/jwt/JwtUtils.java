package com.lb.im.common.domain.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.util.Date;

/**
 * JWT工具类，提供生成、解析和验证JWT令牌的方法
 */
public class JwtUtils {

    /**
     * 生成JWT令牌
     *
     * @param userId 用户ID，作为JWT的接收方（audience）字段
     * @param info 存储在JWT声明中的额外信息
     * @param expireTime 令牌过期时间（秒），从当前时间开始计算
     * @param secret 用于签名的密钥，使用HMAC256算法
     * @return 生成的JWT字符串，若发生异常则返回null
     */
    public static String sign(Long userId, String info, long expireTime, String secret) {
        try {
            Date date = new Date(System.currentTimeMillis() + expireTime * 1000);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withAudience(String.valueOf(userId))
                    .withClaim("info", info)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从JWT令牌中解析用户ID
     *
     * @param token 待解析的JWT令牌字符串
     * @return 用户ID，若解析失败或令牌无效则返回null
     */
    public static Long getUserId(String token) {
        try {
            String userId = JWT.decode(token).getAudience().get(0);
            return Long.parseLong(userId);
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 从JWT令牌中获取存储的额外信息
     *
     * @param token 待解析的JWT令牌字符串
     * @return 存储在"info"声明中的字符串值，若解析失败则返回null
     */
    public static String getInfo(String token) {
        try {
            return JWT.decode(token).getClaim("info").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 验证JWT令牌的签名有效性
     *
     * @param token 待验证的JWT令牌字符串
     * @param secret 用于签名验证的密钥
     * @return 验证成功返回true，否则返回false
     */
    public static Boolean checkSign(String token, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

}
