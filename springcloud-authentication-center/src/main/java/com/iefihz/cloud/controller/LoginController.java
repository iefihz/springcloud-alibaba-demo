package com.iefihz.cloud.controller;

import com.iefihz.cloud.plugins.exception.ExceptionAdvise;
import com.iefihz.cloud.plugins.exception.Res;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@ExceptionAdvise
@RestController
public class LoginController {

    @GetMapping("/test")
    public Res test() {
        return Res.successMessage("test!!!!!");
    }
}
