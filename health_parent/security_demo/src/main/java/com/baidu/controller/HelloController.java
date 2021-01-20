package com.baidu.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("hello")
@PreAuthorize("hasRole('ADMIN')")
// 在类上，代表类中的所有方法必须有ROLE_ADMIN角色才可以访问
public class HelloController {

    @RequestMapping("add")
    @PreAuthorize("hasAuthority('Add')")
    public void add(){
        System.out.println("add...");
    }

    @RequestMapping("update")
    @PreAuthorize("hasRole('ADMIN')")
    public void update(){
        System.out.println("update...");
    }

    @RequestMapping("delete")
    @PreAuthorize("hasAuthority('ABC')")
    public void delete(){
        System.out.println("delete...");
    }
}
