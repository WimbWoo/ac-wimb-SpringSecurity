package com.wim.service;

import com.wim.domain.ResponseResult;
import com.wim.domain.User;

public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
