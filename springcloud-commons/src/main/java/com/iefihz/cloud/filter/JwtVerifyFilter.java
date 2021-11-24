package com.iefihz.cloud.filter;

import com.iefihz.cloud.constant.LoginConstants;
import com.iefihz.cloud.entity.SysUser;
import com.iefihz.cloud.plugins.exception.Res;
import com.iefihz.cloud.plugins.exception.ResCode;
import com.iefihz.cloud.plugins.jwt.JwtPayload;
import com.iefihz.cloud.plugins.jwt.JwtRsaTools;
import com.iefihz.cloud.tools.JsonTools;
import com.iefihz.cloud.tools.JwtRedisTools;
import com.iefihz.cloud.tools.ResponseTools;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * jwt的校验流程
 *
 * @author He Zhifei
 * @date 2020/7/18 2:07
 */
public class JwtVerifyFilter extends BasicAuthenticationFilter {

    private String rsaPublicKey;

    private StringRedisTemplate stringRedisTemplate;

    public JwtVerifyFilter(AuthenticationManager authenticationManager, String rsaPublicKey, StringRedisTemplate stringRedisTemplate) {
        super(authenticationManager);
        this.rsaPublicKey = rsaPublicKey;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 1.校验Header是否包含Authorization并以Bearer+“ ”开头
     * 2.校验token
     * 3.把权限放到SecurityContext
     *
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(LoginConstants.AUTHORIZATION_NAME);
        if (header == null || !header.startsWith(LoginConstants.AUTHORIZATION_PREFIX)) {
            // 如果携带错误的token，则给用户提示请登录
            ResponseTools.writeJson(response, Res.fail(ResCode.NEED_RELOGIN));
            return;
        }
        try {
            // 如果携带了正确格式的token要先得到token
            String token = header.replace(LoginConstants.AUTHORIZATION_PREFIX, "");
            // 验证token是否正确
            JwtRsaTools jwtRsaTools = new JwtRsaTools(rsaPublicKey, null);
            JwtPayload<SysUser> jwtPayload = jwtRsaTools.parseJwt(token, SysUser.class);
            SysUser user = null;
            if (jwtPayload != null && (user = jwtPayload.getUserInfo()) != null) {
                // redis中的token为空，说明被其它操作删除（退出登录、角色/权限修改）
                String authoritiesStr = stringRedisTemplate.opsForValue().get(JwtRedisTools.genJwtRedisKey(user.getUsername()));
                if (authoritiesStr == null) throw new CredentialsExpiredException("认证信息过期，请重新登录");
                List<Map> authoritiesList = JsonTools.toList(authoritiesStr, Map.class);
                List<SimpleGrantedAuthority> authorities = Optional.ofNullable(authoritiesList).orElse(Collections.emptyList()).stream()
                        .map(m -> new SimpleGrantedAuthority((String) m.get("authority"))).collect(Collectors.toList());

                UsernamePasswordAuthenticationToken authResult = new UsernamePasswordAuthenticationToken(
                        user.getUsername(), null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authResult);
                chain.doFilter(request, response);
                return;
            }
            ResponseTools.writeJson(response, Res.fail(ResCode.NEED_RELOGIN));
        } catch (Exception e) {
            ResponseTools.writeJson(response, Res.fail(ResCode.NEED_RELOGIN));
        }
    }

}