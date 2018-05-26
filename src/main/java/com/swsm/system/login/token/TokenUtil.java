package com.swsm.system.login.token;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * ClassName: TokenUtil
 * </p>
 * <p>
 * Description: token工具类
 * </p>
 */
public class TokenUtil {
    /**
     * jwtTokenSecret
     */
    private final static String SECRET = "jwtTokenSecret";

    /**
     * 
     * <p>
     * Description: 创建token
     * </p>
     * 
     * @param userName 用户名
     * @param password 密码
     * @return 返回token
     */
    public static String createJWT(String userName, String password) {
        SignatureAlgorithm signatureAlgorithm;
        signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes;
        apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET);
        Key signingKey;
        signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        Map<String, Object> claims;
        claims = new HashMap<String, Object>();
        claims.put("userName", userName);
        claims.put("pwd", password);
        JwtBuilder builder;
        builder = Jwts.builder().setClaims(claims).signWith(signatureAlgorithm, signingKey);
        return builder.compact();
    }

    /**
     * 
     * <p>
     * Description: 根据token获取用户信息
     * </p>
     * 
     * @param jwt jwt
     * @return 用户信息
     */
    public static String[] getUserInfo(String jwt) {
        Claims claims;
        claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(SECRET)).parseClaimsJws(jwt).getBody();
        String[] arr;
        arr = new String[2];
        arr[0] = claims.get("userName") + "";
        arr[1] = claims.get("pwd") + "";
        return arr;
    }

}
