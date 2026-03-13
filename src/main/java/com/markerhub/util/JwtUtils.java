package com.markerhub.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.markerhub.common.Constant;
import com.markerhub.common.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import com.auth0.jwt.interfaces.DecodedJWT;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * jwt工具类
 */
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "markerhub.jwt")
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    private static String secret="zsh123456";
    private long expire;
    private String header;


    /**
     * 过期时间改为从配置文件获取
     */
    private static String accessTokenExpireTime;
    @Value("${accessTokenExpireTime}")
    public void setAccessTokenExpireTime(String accessTokenExpireTime) {
        JwtUtils.accessTokenExpireTime = accessTokenExpireTime;
    }

    /**
     * 生成jwt token
     */
    public String generateToken(long userId) {
        Date nowDate = new Date();
        //过期时间
        Date expireDate = new Date(nowDate.getTime() + expire * 1000);
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(userId+"")
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret) // 签名
                .compact();
    }
    /**
     * 生成 JWT（仅 account，无 RefreshToken 机制）
     */
    public static String sign(String account) {
        try {
            Date date = new Date(System.currentTimeMillis() + Long.parseLong(accessTokenExpireTime) * 1000);
            return Jwts.builder()
                    .setHeaderParam("typ", "JWT")
                    .setExpiration(date)
                    .claim("account", account)
                    .signWith(SignatureAlgorithm.HS512, secret)
                    .compact();
        } catch (Exception e) {
            logger.error("JWTToken加密出现异常:{}", e.getMessage());
            throw new CustomException("JWTToken加密出现异常:" + e.getMessage());
        }
    }

    public Claims getClaimByToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            log.debug("validate is token error ", e);
            return null;
        }
    }

    /**
     * 获得Token中的信息无需secret解密也能获得
     * @param token
     * @param claim
     * @return java.lang.String
     * @author Wang926454
     * @date 2018/9/7 16:54
     */
    public static String getClaim(String token, String claim) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            // 只能输出String类型，如果是其他类型返回null
            return jwt.getClaim(claim).asString();
        } catch (JWTDecodeException e) {
            logger.error("解密Token中的公共信息-"+token+"出现JWTDecodeException异常:{}", e.getMessage());
            throw new CustomException("解密Token中的公共信息出现JWTDecodeException异常:" + e.getMessage());
        } catch (Exception e) {
            // 捕获其他未预见的异常
            logger.error("处理Token时发生未知错误: {}", e.getMessage(), e);
            throw new CustomException("处理Token时发生未知错误，错误信息：" + e.getMessage());
        }
    }

    /**
     * token是否过期
     * @return  true：过期
     */
    public boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }


}
