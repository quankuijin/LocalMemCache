package com.example.cache.controller;

import com.example.cache.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cache")
public class CacheController {

    private final CacheService cacheService;

    @Autowired
    public CacheController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @GetMapping("/{key}")
    public ResponseEntity<Map<String, Object>> get(@PathVariable String key) {
        String value = cacheService.get(key);
        Map<String, Object> response = new HashMap<>();

        if (value != null) {
            response.put("success", true);
            response.put("key", key);
            response.put("value", value);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Key not found: " + key);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/{key}")
    public ResponseEntity<Map<String, Object>> put(
            @PathVariable String key,
            @RequestBody Map<String, String> request) {
        String value = request.get("value");
        Map<String, Object> response = new HashMap<>();

        if (value == null) {
            response.put("success", false);
            response.put("message", "Value is required");
            return ResponseEntity.badRequest().body(response);
        }

        cacheService.put(key, value);
        response.put("success", true);
        response.put("key", key);
        response.put("value", value);
        response.put("message", "Cache entry created/updated successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable String key) {
        Map<String, Object> response = new HashMap<>();
        cacheService.delete(key);
        response.put("success", true);
        response.put("key", key);
        response.put("message", "Cache entry deleted successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("size", cacheService.size());
        response.put("capacity", cacheService.getCapacity());
        return ResponseEntity.ok(response);
    }
}
