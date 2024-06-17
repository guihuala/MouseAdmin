package com.example.adminapi.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.adminapi.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtUtil jwtUtil;

    private static final Logger logger = LoggerFactory.getLogger(JwtInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true; // 预检请求直接放行
        }

        String header = request.getHeader("Authorization");
        logger.info("Intercepting request: {}", request.getRequestURI());
        if (header == null) {
            logger.warn("Authorization header is missing");
        } else {
            logger.info("Authorization header: {}", header);
        }
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.getUsernameFromToken(token);
                request.setAttribute("username", username);
                return true; // 放行, 继续执行后续的操作
            } else {
                return unauthorizedResponse(response, "Invalid or expired token");
            }
        } else {
            return unauthorizedResponse(response, "Missing or invalid Authorization header");
        }
    }

    // 401 未授权
    private boolean unauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);// 401
        response.setContentType("application/json");// 返回json格式
        response.setCharacterEncoding("UTF-8");// 设置编码格式
        response.getWriter().write(message);
        response.getWriter().flush();
        return false;
    }
}