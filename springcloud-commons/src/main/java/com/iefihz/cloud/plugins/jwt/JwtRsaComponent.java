package com.iefihz.cloud.plugins.jwt;

import com.iefihz.cloud.tools.JsonTools;
import com.iefihz.cloud.tools.RsaTools;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.crypto.DefaultJwtSigner;
import io.jsonwebtoken.impl.crypto.JwtSigner;
import io.jsonwebtoken.io.Encoders;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.UUID;

/**
 * Jwt 使用Rsa算法的实现类
 */
@Component
public class JwtRsaComponent implements BaseJwtComponent {

    private static final String USER_INFO_KEY = "userInfo";

    @Autowired
    private JwtProperties properties;

    private JwtBuilder jwtBuilder;

    private JwtParser jwtParser;

    private JwtSigner signer;

    /**
     * 初始化jwtBuilder、jwtParser
     */
    @PostConstruct
    public void init() throws Exception {
//        // SHA512
//        String auth = "0123456789012345678901234567890123456789012345678901234567890123456789";
//        jwtBuilder = Jwts.builder()
//                .setHeaderParam("typ", "JWT")
//                .setHeaderParam("alg", "SHA512")
//                .signWith(Keys.hmacShaKeyFor(auth.getBytes()), SignatureAlgorithm.HS512);
//        jwtParser = Jwts.parserBuilder()
//                .setSigningKey(Keys.hmacShaKeyFor(auth.getBytes()))
//                .build();

        // RSA-SHA256，允许公私钥为空的原因：有些地方只提供了私钥或者公钥，但也允许使用里面的部分方法
        if (StringUtils.isNotBlank(properties.getRsaPublicKey())) {
            jwtParser = Jwts.parserBuilder()
                    .setSigningKey(RsaTools.fromPublicKeyStr(properties.getRsaPublicKey()))
                    .build();
        }
        if (StringUtils.isNotBlank(properties.getRsaPrivateKey())) {
            jwtBuilder = Jwts.builder()
                    .setHeaderParam("typ", "JWT")
                    .setHeaderParam("alg", "RS256")
                    .signWith(RsaTools.fromPrivateKeyStr(properties.getRsaPrivateKey()), SignatureAlgorithm.RS256);
            signer = new DefaultJwtSigner(SignatureAlgorithm.RS256, RsaTools.fromPrivateKeyStr(properties.getRsaPrivateKey()), Encoders.BASE64URL);
        }
    }

    @Override
    public String generateJwt(Object userInfo) {
        return jwtBuilder
                .setId(UUID.randomUUID().toString())
                .claim(USER_INFO_KEY, JsonTools.toString(userInfo))
                .compact();
    }

    @Override
    public String generateJwt(Object userInfo, int expire) {
        return jwtBuilder
                .setId(UUID.randomUUID().toString())
                .setExpiration(new Date(System.currentTimeMillis() + expire * 1000))
                .claim(USER_INFO_KEY, JsonTools.toString(userInfo))
                .compact();
    }

    @Override
    public Jws<Claims> parseJwt(String token) {
        return jwtParser.parseClaimsJws(token);
    }

    @Override
    public <T> JwtPayload<T> parseJwt(String token, Class<T> userType) {
        Jws<Claims> claimsJws = parseJwt(token);
        Claims body = claimsJws.getBody();
        JwtPayload<T> payload = new JwtPayload<>();
        payload.setJwtId(body.getId());
        payload.setExpiration(body.getExpiration());
        payload.setUserInfo(JsonTools.toBean(body.get(USER_INFO_KEY).toString(), userType));
        return payload;
    }

    // 不能判断是否超时，只能判断是否合法
    @Override
    public Boolean verifyJwt(String token) {
        try {
            String[] arr = null;
            if (StringUtils.isBlank(token) ||  (arr = token.split("[.]")).length != 3) {
                return false;
            }
            String base64UrlSignature = signer.sign(arr[0] + "." + arr[1]);
            return StringUtils.isNotBlank(base64UrlSignature) && base64UrlSignature.equals(arr[2]);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 这里只捕获{@link ExpiredJwtException}异常的原因：
     * 不能保证每个开发人员都进行了合法性校验{@link JwtRsaComponent#verifyJwt(String)}后，
     * 再进行有效期的校验，因此如果开发人员传入了非法的token，这里会往外抛出其它异常，从而终止请求
     *
     * @param token
     * @return
     */
    @Override
    public Boolean isExpired(String token) {
        boolean isExpired = false;
        try {
            parseJwt(token);
        } catch (ExpiredJwtException e) {
            isExpired = true;
        }
        return isExpired;
    }

}
