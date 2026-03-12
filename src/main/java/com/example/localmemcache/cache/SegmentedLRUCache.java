package com.example.localmemcache.cache;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class SegmentedLRUCache implements InitializingBean {

    @Value("${cache.max.size:5000}")
    private int maxSize;

    private static final int SEGMENT_COUNT = 16;
    private Segment[] segments;

    private static class Segment {
        final LinkedHashMap<String, String> map;
        final Lock lock = new ReentrantLock();
        final int maxSize;

        Segment(int maxSize) {
            this.maxSize = maxSize;
            this.map = new LinkedHashMap<String, String>(maxSize, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                    return size() > Segment.this.maxSize;
                }
            };
        }
    }

    @Override
    public void afterPropertiesSet() {
        segments = new Segment[SEGMENT_COUNT];
        int segmentSize = Math.max(1, maxSize / SEGMENT_COUNT);
        for (int i = 0; i < SEGMENT_COUNT; i++) {
            segments[i] = new Segment(segmentSize);
        }
    }

    private Segment getSegment(String key) {
        int hash = Math.abs(key.hashCode() % SEGMENT_COUNT);
        return segments[hash];
    }

    public String get(String key) {
        Segment seg = getSegment(key);
        seg.lock.lock();
        try {
            return seg.map.get(key);
        } finally {
            seg.lock.unlock();
        }
    }

    public void put(String key, String value) {
        Segment seg = getSegment(key);
        seg.lock.lock();
        try {
            seg.map.put(key, value);
        } finally {
            seg.lock.unlock();
        }
    }

    public void delete(String key) {
        Segment seg = getSegment(key);
        seg.lock.lock();
        try {
            seg.map.remove(key);
        } finally {
            seg.lock.unlock();
        }
    }

    public int size() {
        int total = 0;
        for (Segment seg : segments) {
            seg.lock.lock();
            try {
                total += seg.map.size();
            } finally {
                seg.lock.unlock();
            }
        }
        return total;
    }
}
