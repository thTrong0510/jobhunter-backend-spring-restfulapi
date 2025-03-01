package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    // @CrossOrigin cách fix CORS cho từng API
    public String getHelloWorld() {
        return "Hello World (Hỏi Dân IT & Eric)";
    }
}
