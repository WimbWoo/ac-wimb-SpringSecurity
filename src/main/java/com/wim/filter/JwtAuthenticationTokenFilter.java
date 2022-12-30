package com.wim.filter;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.wim.domain.LoginUser;
import com.wim.utils.JwtUtil;
import com.wim.utils.RedisCache;
import io.jsonwebtoken.Claims;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 获取token
        String token = request.getHeader("token");
        if (StringUtil.isNullOrEmpty(token)) {
            // 放行
            filterChain.doFilter(request, response);
            return;
        }
        // 从token解析userid
        String userId;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = claims.getSubject();
        } catch (Exception e) {
            throw new RuntimeException("token 非法");
        }

        // 从redis中获取用户信息
        String key = "login:" + userId;
        LoginUser loginUser = redisCache.getCacheObject(key);
        if (ObjectUtils.isNull(loginUser)) {
            throw new RuntimeException("用户未登录");
        }
        // 存入SecurityContextHolder中
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        // 放行
        filterChain.doFilter(request, response);
    }
}
