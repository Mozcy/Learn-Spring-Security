package sbs.getcry.filter;


import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sbs.getcry.bo.LoginUserDetails;
import sbs.getcry.common.TokenService;
import sbs.getcry.utils.JWTUtils;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Admin-Token";
    private static List<String> EXCLUDE_URL = List.of("/user/login","/user/register","/user/logout");

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        // 如果请求的URI在排除列表中, 则直接放行
        if (EXCLUDE_URL.contains(uri)) {
            filterChain.doFilter(request, response);
        } else {
            // 获取请求中携带的 Token
            String token = request.getHeader(AUTHORIZATION_HEADER);
            // 如果没有token则抛出异常
            if (StringUtils.isEmpty(token)) {
                throw new InsufficientAuthenticationException("认证错误, 令牌为空!");
            }

            String bearer = token.replace("Bearer ", "");
            String userKey;
            try {
                userKey = JWTUtils.getUserKey(bearer);
            } catch (ExpiredJwtException | MalformedJwtException e) {
                throw new BadCredentialsException("认证错误, 令牌校验失败!");
            }
            LoginUserDetails loginUser = tokenService.getLoginUser(userKey);
            if (loginUser == null) {
                throw new BadCredentialsException("认证错误, 用户未登录!");
            }
            // 注意: 每次认证都会在 SecurityContextHolder 中放入当前用户的认证信息, 当这个请求结束时 SecurityContextPersistenceFilter 会自动清除这个上下文
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities()));
            filterChain.doFilter(request, response);
        }
    }
}
