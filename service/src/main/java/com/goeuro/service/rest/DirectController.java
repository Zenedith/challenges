package com.goeuro.service.rest;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class DirectController {

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

}