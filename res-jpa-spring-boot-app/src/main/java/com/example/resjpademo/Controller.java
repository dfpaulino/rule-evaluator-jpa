package com.example.resjpademo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by dp on 14/02/2021
 */
@RestController
public class Controller {
    @GetMapping("/")
    public String get() {
        return "bla";
    }
}
