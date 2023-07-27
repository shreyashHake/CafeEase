package com.cafe.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CafeUtils {
    private CafeUtils() {

    }

    public static ResponseEntity<String> getResoponseEntity(String message, HttpStatus status) {
        return new ResponseEntity<>("{\"message\" : \"" + message + "\"}", status);
    }
}
