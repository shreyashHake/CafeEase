package com.cafe.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface CategoryService {
    ResponseEntity<String> addCategory(Map<String, String> requestMap);
}
