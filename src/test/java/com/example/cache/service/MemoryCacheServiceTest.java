package com.example.cache.service;

import com.example.cache.config.CacheProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemoryCacheServiceTest {

    private MemoryCacheService cacheService;

    @BeforeEach
    void setUp() {
        CacheProperties properties = new CacheProperties();
        properties.setMaxSize(3);
        cacheService = new MemoryCacheService(properties);
    }

    @Test
    void testPutAndGet() {
        cacheService.put("key1", "value1");
        assertEquals("value1", cacheService.get("key1"));
    }

    @Test
    void testGetNonExistentKey() {
        assertNull(cacheService.get("nonexistent"));
    }

    @Test
    void testDelete() {
        cacheService.put("key1", "value1");
        assertTrue(cacheService.delete("key1"));
        assertNull(cacheService.get("key1"));
    }

    @Test
    void testDeleteNonExistentKey() {
        assertFalse(cacheService.delete("nonexistent"));
    }

    @Test
    void testLRUEviction() {
        cacheService.put("key1", "value1");
        cacheService.put("key2", "value2");
        cacheService.put("key3", "value3");
        
        cacheService.get("key1");
        
        cacheService.put("key4", "value4");
        
        assertNull(cacheService.get("key2"));
        assertEquals("value1", cacheService.get("key1"));
        assertEquals("value3", cacheService.get("key3"));
        assertEquals("value4", cacheService.get("key4"));
    }

    @Test
    void testSize() {
        assertEquals(0, cacheService.size());
        cacheService.put("key1", "value1");
        assertEquals(1, cacheService.size());
        cacheService.put("key2", "value2");
        assertEquals(2, cacheService.size());
    }

    @Test
    void testUpdateExistingKey() {
        cacheService.put("key1", "value1");
        cacheService.put("key1", "value2");
        assertEquals("value2", cacheService.get("key1"));
        assertEquals(1, cacheService.size());
    }
}
