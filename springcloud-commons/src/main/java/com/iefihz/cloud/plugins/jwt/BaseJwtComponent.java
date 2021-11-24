package com.iefihz.cloud.plugins.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

/**
 * Jwt生成、校验基类
 */
public interface BaseJwtComponent {

    /**
     * 生成携带用户数据的Jwt
     *
     * @param userInfo
     * @return
     */
    String generateJwt(Object userInfo);

    /**
     * 生成携带用户数据的Jwt，并指定过期时间，单位：秒
     *
     * @param userInfo
     * @param expire
     * @return
     */
    String generateJwt(Object userInfo, int expire);

    /**
     * 解析Jwt
     *
     * @param token
     * @return
     */
    Jws<Claims> parseJwt(String token);

    /**
     * 将Payload中的用户信息封装到{@link JwtPayload}
     *
     * @param token
     * @param userType
     * @param <T>
     * @return 封装数据
     */
    <T> JwtPayload<T> parseJwt(String token, Class<T> userType);

    /**
     * 验证token是否合法，用header中指定的算法对 （headerBase64URL+"."+payloadBase64URL） 加密，对比sign与签名后的结果
     *
     * @param token
     * @return 检验结果
     */
    Boolean verifyJwt(String token);

    /**
     * 有效性校验，尽可能在此之前进行合法性校验
     *
     * @param token
     * @return
     */
    Boolean isExpired(String token);
}