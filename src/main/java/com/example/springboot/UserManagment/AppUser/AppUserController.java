package com.example.springboot.UserManagment.AppUser;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/users")
public class AppUserController {

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public String get() {
        return "hello-world";
    }
}
