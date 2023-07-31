package com.cafe.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;

public class CafeUtils {
    private CafeUtils() {
    }

    public static ResponseEntity<String> getResoponseEntity(String message, HttpStatus status) {
        return new ResponseEntity<>("{\"message\" : \"" + message + "\"}", status);
    }

    public static String getUUID() {
        Date date = new Date();
        return "BILL-" + date.getTime();
    }
}
