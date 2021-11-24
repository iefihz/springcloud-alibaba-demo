package com.iefihz.cloud.handler;

import com.iefihz.cloud.plugins.exception.Res;
import com.iefihz.cloud.plugins.exception.ResCode;
import com.iefihz.cloud.tools.ResponseTools;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录失败处理器
 */
@Component
public class AuthFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        Res res = null;
        if (e instanceof UsernameNotFoundException) {
            res = Res.failMessage(e.getMessage());
        } else if (e instanceof BadCredentialsException) {
            res = Res.fail(ResCode.INCORRECT_CREDENTIALS);
        } else {
            res = Res.failMessage("用户名或密码输入有误");
        }
        ResponseTools.writeJson(response, res);
    }
}
