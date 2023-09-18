package fr.babaprog.spring.boot.tutorial.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Value("${hello.world}")
    private String welcomeMessage;

    @GetMapping("/")
    public String helloWorld() {
        return welcomeMessage;
    }
}
