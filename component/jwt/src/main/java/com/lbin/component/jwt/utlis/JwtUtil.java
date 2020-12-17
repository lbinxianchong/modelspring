package com.lbin.component.jwt.utlis;

import cn.hutool.core.util.RandomUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.lbin.common.config.EncryptConfig;
import com.lbin.common.exception.ResultException;
import com.lbin.common.util.HttpServletUtil;
import com.lbin.component.jwt.enums.JwtResultEnums;
import com.lbin.server.system.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;

/**
 * @author
 * @date 2019/4/9
 */
public class JwtUtil {

    /**
     * 生成JwtToken
     * @param username 用户名
     * @param secret 秘钥
     * @param amount 过期天数
     */
    public static String getToken(String username, String secret, int amount){
        User user = new User();
        user.setUsername(username);
        return getToken(user, secret, amount);
    }

    /**
     * 生成JwtToken
     * @param user 用户对象
     * @param secret 秘钥
     * @param amount 过期天数
     */
    public static String getToken(User user, String secret, int amount){
        // 过期时间
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DATE, amount);

        // 随机Claim
        String random = RandomUtil.randomString(12);

        // 创建JwtToken对象
        String token="";
        token= JWT.create()
                // 用户名
                .withSubject(user.getUsername())
                // 发布时间
                .withIssuedAt(new Date())
                // 过期时间
                .withExpiresAt(ca.getTime())
                // 自定义随机Claim
                .withClaim("ran", random)
                .sign(getSecret(secret, random));

        return token;
    }

    /**
     * 获取请求对象中的token数据
     */
    public static String getRequestToken(HttpServletRequest request){
        // 获取JwtTokens失败
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new ResultException(JwtResultEnums.TOKEN_ERROR);
        }
        return authorization.substring(7);
    }

    /**
     * 获取当前token中的用户名
     */
    public static String getSubject(){
        HttpServletRequest request = HttpServletUtil.getRequest();
        String token = getRequestToken(request);
        return JWT.decode(token).getSubject();
    }

    public static void verifyToken(String token) throws JWTVerificationException {
        verifyToken(token, EncryptConfig.secret);
    }
    /**
     * 验证JwtToken
     * @param token JwtToken数据
     * @return true 验证通过
     * @exception TokenExpiredException Token过期
     * @exception JWTVerificationException 令牌无效（验证不通过）
     */
    public static void verifyToken(String token, String secret) throws JWTVerificationException {
        String ran = JWT.decode(token).getClaim("ran").asString();
        JWTVerifier jwtVerifier = JWT.require(getSecret(secret, ran)).build();
        jwtVerifier.verify(token);
    }

    /**
     * 生成Secret混淆数据
     */
    private static Algorithm getSecret(String secret, String random){
        String salt = "正是江南好风景落花时节又逢君";
        return Algorithm.HMAC256(secret + salt + "509e29d37f8d4a748c5862bda71e56e8" + random);
    }
}
