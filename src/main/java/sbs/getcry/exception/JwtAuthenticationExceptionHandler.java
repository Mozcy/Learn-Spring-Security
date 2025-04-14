package sbs.getcry.exception;

import com.alibaba.fastjson.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import sbs.getcry.common.R;

import java.io.IOException;

/**
 * @Author : saeko
 * @Date: 2025/4/9 13:40
 * @Description: 异常捕获, 用于处理权限异常问题
 */

@Component
public class JwtAuthenticationExceptionHandler implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 设置响应的内容类型为 JSON
        response.setContentType("application/json;charset=UTF-8");

        // 设置 HTTP 状态码为 401 (未授权)
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // 封装响应数据
        R r = R.of(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage(), null);
        String result = JSON.toJSONString(r);

        // 将响应数据写入响应体
        response.getWriter().write(result);
    }
}
