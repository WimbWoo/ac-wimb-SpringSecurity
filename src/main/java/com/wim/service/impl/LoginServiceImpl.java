package com.wim.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.wim.domain.LoginUser;
import com.wim.domain.ResponseResult;
import com.wim.domain.User;
import com.wim.service.LoginService;
import com.wim.utils.JwtUtil;
import com.wim.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        // AuthenticationManager 的 authenticate 方法 对用户进行认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        // 认证没有通过，给出对应提示
        if (ObjectUtils.isNull(authenticate)) {
            throw new RuntimeException("登录失败");
        }
        // 认证通过，使用 userid 生成 jwt， jwt存入 ResponseResult 作为返回
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
        // 将完整的用户信息存入 redis， userid 作为 key
        redisCache.setCacheObject("login:" + userId, loginUser);
        return new ResponseResult(200, "登录成功", map);
    }

    // 失效 token 是否需要删除 securityContext 中的对应token
    @Override
    public ResponseResult logout() {
        // 从 SecurityContext 中获取用户 id
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        // 从 Redis 中删除用户的token
        redisCache.deleteObject("login:" + userId);
        return new ResponseResult(Integer.valueOf("200"), "注销成功");
    }
}
