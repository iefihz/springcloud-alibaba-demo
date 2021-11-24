package com.iefihz.cloud.handler;

import com.iefihz.cloud.constant.LoginConstants;
import com.iefihz.cloud.entity.SecurityUser;
import com.iefihz.cloud.entity.SysUser;
import com.iefihz.cloud.plugins.exception.Res;
import com.iefihz.cloud.plugins.jwt.JwtProperties;
import com.iefihz.cloud.plugins.jwt.JwtRsaComponent;
import com.iefihz.cloud.tools.JsonTools;
import com.iefihz.cloud.tools.JwtRedisTools;
import com.iefihz.cloud.tools.ResponseTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 登录成功处理器
 */
@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtRsaComponent jwtRsaComponent;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 1.通过用户信息-rsa颁发证书（token）
     * 2.把token放到请求头
     *
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try {
            SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
            SysUser sysUser = securityUser.getSysUser();
            // 密码设置为null
            sysUser.setPassword(null);
            // 这里只通过用户ID和用户名生成token，并存放到redis，value为认证后的权限（准确来说是用户的角色与权限的集合）
            SysUser userInfo = new SysUser();
            userInfo.setUserId(sysUser.getUserId());
            userInfo.setUsername(sysUser.getUsername());
            String token = jwtRsaComponent.generateJwt(userInfo, jwtProperties.getTokenValiditySeconds());
            sysUser.setToken(LoginConstants.AUTHORIZATION_PREFIX + token);

            stringRedisTemplate.opsForValue().set(JwtRedisTools.genJwtRedisKey(sysUser.getUsername()), JsonTools.toString(securityUser.getAuthorities()),
                    jwtProperties.getTokenValiditySeconds(), TimeUnit.SECONDS);
            ResponseTools.writeJson(response, Res.success("认证成功", sysUser));
        } catch (Exception e) {
            e.printStackTrace();
            ResponseTools.writeJson(response, Res.failMessage("证书颁发异常"));
            return;
        }
    }
}
