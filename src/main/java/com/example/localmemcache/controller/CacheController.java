package com.example.localmemcache.controller;

import com.example.localmemcache.cache.LRUCache;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cache")
public class CacheController {

    private final LRUCache cache;

    public CacheController(LRUCache cache) {
        this.cache = cache;
    }

    @GetMapping("/{key}")
    public String get(@PathVariable String key) {
        return cache.get(key);
    }

    @PostMapping("/{key}")
    public void put(@PathVariable String key, @RequestParam String value) {
        cache.put(key, value);
    }

    @DeleteMapping("/{key}")
    public void delete(@PathVariable String key) {
        cache.delete(key);
    }

    @GetMapping("/size")
    public int size() {
        return cache.size();
    }
}
