package com.iefihz.cloud.tools;

import com.iefihz.cloud.constant.LoginConstants;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class JwtRedisTools {

    /**
     * 生成规则：前缀+md5(username)+"_"+md5(user-agent)
     *
     * @param username
     * @return
     */
    public static final String genJwtRedisKey(String username) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return new StringBuilder(LoginConstants.REDIS_JWT_PREFIX)
                .append(Md5Tools.generate(username)).append("_")
                .append(Md5Tools.generate(request.getHeader("User-Agent"))).toString();
    }
}
