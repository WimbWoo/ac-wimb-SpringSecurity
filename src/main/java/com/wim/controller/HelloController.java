package com.wim.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @PostMapping("/hello")
//  自定义权限校验方法  @PreAuthorize("@ex.hasAuthority('system:dept:list')")
    @PreAuthorize("hasAuthority('system:test:list')")
    public String hello(){
        return "hello";
    }
}


