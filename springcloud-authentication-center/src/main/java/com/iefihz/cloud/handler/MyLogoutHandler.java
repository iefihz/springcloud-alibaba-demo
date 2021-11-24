package com.iefihz.cloud.handler;

import com.iefihz.cloud.constant.LoginConstants;
import com.iefihz.cloud.entity.SysUser;
import com.iefihz.cloud.plugins.exception.Res;
import com.iefihz.cloud.plugins.jwt.JwtPayload;
import com.iefihz.cloud.plugins.jwt.JwtRsaComponent;
import com.iefihz.cloud.tools.JwtRedisTools;
import com.iefihz.cloud.tools.ResponseTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 退出登录处理器
 */
@Component
public class MyLogoutHandler implements LogoutHandler {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private JwtRsaComponent jwtRsaComponent;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            // 清除redis中的缓存
            String token = request.getHeader(LoginConstants.AUTHORIZATION_NAME).replace(LoginConstants.AUTHORIZATION_PREFIX, "");
            JwtPayload<SysUser> sysUser = jwtRsaComponent.parseJwt(token, SysUser.class);
            stringRedisTemplate.delete(JwtRedisTools.genJwtRedisKey(sysUser.getUserInfo().getUsername()));
        } catch (Exception e) {
        }
        ResponseTools.writeJson(response, Res.successMessage("已退出登录"));
    }
}
