package com.cafe.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping(path = "/user")
public interface UserRest {
    @PostMapping("/signUp")
    ResponseEntity<String> signUp(@RequestBody(required = true)Map<String, String> requestMap);

    @PostMapping("/login")
    ResponseEntity<String> login(@RequestBody(required = true)Map<String, String> requestMap);
}
