package com.example.cache.controller;

import com.example.cache.service.MemoryCacheService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cache")
public class CacheController {

    private final MemoryCacheService cacheService;

    public CacheController(MemoryCacheService cacheService) {
        this.cacheService = cacheService;
    }

    @PutMapping("/{key}")
    public ResponseEntity<String> put(@PathVariable String key, @RequestBody String value) {
        cacheService.put(key, value);
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/{key}")
    public ResponseEntity<String> get(@PathVariable String key) {
        String value = cacheService.get(key);
        if (value == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(value);
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<String> delete(@PathVariable String key) {
        boolean deleted = cacheService.delete(key);
        if (deleted) {
            return ResponseEntity.ok("OK");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/size")
    public ResponseEntity<Integer> size() {
        return ResponseEntity.ok(cacheService.size());
    }
}
