package com.restApi.RestAPI;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApiController {

    @GetMapping("/test")
    public String getResponse() {
        return "masuk sini???";
    }
}
