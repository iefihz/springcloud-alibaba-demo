package com.iefihz.cloud.plugins.jwt;

import java.util.Date;

/**
 * 为了方便后期获取token中的用户信息，将token中载荷部分单独封装成一个对象
 */
public class JwtPayload<T> {

    /**
     * 载荷id，不是userId
     */
    private String jwtId;

    /**
     * 用户信息
     */
    private T userInfo;

    /**
     * 过期时间
     */
    private Date expiration;

    public String getJwtId() {
        return jwtId;
    }

    public void setJwtId(String jwtId) {
        this.jwtId = jwtId;
    }

    public T getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(T userInfo) {
        this.userInfo = userInfo;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }
}