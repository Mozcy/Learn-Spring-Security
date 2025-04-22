package sbs.getcry.exception;

import com.alibaba.fastjson.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import sbs.getcry.common.R;

import java.io.IOException;

/**
 * @Author : saeko
 * @Date: 2025/4/22 18:42
 * @Description: 统一处理权限异常
 */

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 设置响应的内容类型为 JSON
        response.setContentType("application/json;charset=UTF-8");

        // 设置 HTTP 状态码为 403 (无权限)
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        // 封装响应数据
        R r = R.of(HttpServletResponse.SC_FORBIDDEN, "无权限访问!", null);
        String result = JSON.toJSONString(r);

        // 将响应数据写入响应体
        response.getWriter().write(result);

    }
}
