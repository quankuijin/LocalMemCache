package com.example.localmemcache.cache;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class LRUCache implements InitializingBean {

    @Value("${cache.max.size:5000}")
    private int maxSize;

    private LinkedHashMap<String, String> cache;
    private final Lock lock = new ReentrantLock();

    @Override
    public void afterPropertiesSet() {
        cache = new LinkedHashMap<String, String>(maxSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                return size() > maxSize;
            }
        };
    }

    public String get(String key) {
        lock.lock();
        try {
            return cache.get(key);
        } finally {
            lock.unlock();
        }
    }

    public void put(String key, String value) {
        lock.lock();
        try {
            cache.put(key, value);
        } finally {
            lock.unlock();
        }
    }

    public void delete(String key) {
        lock.lock();
        try {
            cache.remove(key);
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        lock.lock();
        try {
            return cache.size();
        } finally {
            lock.unlock();
        }
    }
}
