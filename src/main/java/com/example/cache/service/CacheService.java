package com.example.cache.service;

import com.example.cache.lru.LruCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class CacheService {

    @Value("${cache.capacity:5000}")
    private int capacity;

    private volatile LruCache<String, String> cache;

    @PostConstruct
    public void init() {
        this.cache = new LruCache<>(capacity);
    }

    public String get(String key) {
        return cache.get(key);
    }

    public void put(String key, String value) {
        cache.put(key, value);
    }

    public void delete(String key) {
        cache.delete(key);
    }

    public boolean containsKey(String key) {
        return cache.containsKey(key);
    }

    public int size() {
        return cache.size();
    }

    public void clear() {
        cache.clear();
    }

    public int getCapacity() {
        return capacity;
    }
}
