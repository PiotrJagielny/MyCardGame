package com.example.demo;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path ="/ttt")
public class TestController {

    @GetMapping(path = "getGet")
    @CrossOrigin
    public String testMapping() {
        return "This spring boot app works";
    }
}
