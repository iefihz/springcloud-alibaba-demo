package com.iefihz.cloud.controller;

import com.iefihz.cloud.plugins.exception.ExceptionAdvise;
import com.iefihz.cloud.plugins.exception.Res;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ExceptionAdvise
@RestController
@RequestMapping("/storage")
public class StorageController {

    @Value("${server.port}")
    private String port;

    @GetMapping("/port")
    public Res port() {
        return Res.successData("StorageController.port: " + port);
    }

    @GetMapping("/e")
    public Res e() {
        throw new RuntimeException("eee");
    }
}
