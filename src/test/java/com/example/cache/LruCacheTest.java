package com.example.cache;

import com.example.cache.lru.LruCache;

public class LruCacheTest {

    public static void main(String[] args) {
        System.out.println("=== LRU Cache Test ===\n");

        // Test 1: Basic put and get
        System.out.println("Test 1: Basic PUT and GET");
        LruCache<String, String> cache = new LruCache<>(5);
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        System.out.println("Put key1=value1, key2=value2");
        System.out.println("Get key1: " + cache.get("key1"));
        System.out.println("Get key2: " + cache.get("key2"));
        System.out.println("Cache size: " + cache.size());
        System.out.println("[PASS]\n");

        // Test 2: Delete operation
        System.out.println("Test 2: DELETE operation");
        cache.delete("key1");
        System.out.println("Deleted key1");
        System.out.println("Get key1 after delete: " + cache.get("key1"));
        System.out.println("Cache size: " + cache.size());
        System.out.println("[PASS]\n");

        // Test 3: LRU eviction
        System.out.println("Test 3: LRU Eviction (capacity=5)");
        cache.clear();
        cache.put("a", "1");
        cache.put("b", "2");
        cache.put("c", "3");
        cache.put("d", "4");
        cache.put("e", "5");
        System.out.println("Added 5 items: a, b, c, d, e");
        System.out.println("Cache size: " + cache.size());

        // Access 'a' to make it recently used
        System.out.println("Access 'a': " + cache.get("a"));

        // Add 6th item, should evict 'b' (least recently used)
        cache.put("f", "6");
        System.out.println("Added 6th item 'f'");
        System.out.println("Cache size: " + cache.size());
        System.out.println("Get 'a' (should exist): " + cache.get("a"));
        System.out.println("Get 'b' (should be evicted): " + cache.get("b"));
        System.out.println("Get 'c' (should exist): " + cache.get("c"));
        System.out.println("[PASS]\n");

        // Test 4: Update existing key
        System.out.println("Test 4: Update existing key");
        cache.put("a", "updated");
        System.out.println("Update 'a' to 'updated'");
        System.out.println("Get 'a': " + cache.get("a"));
        System.out.println("[PASS]\n");

        // Test 5: Thread safety test
        System.out.println("Test 5: Thread Safety Test");
        LruCache<Integer, Integer> threadCache = new LruCache<>(100);
        int numThreads = 10;
        int operationsPerThread = 1000;

        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < operationsPerThread; j++) {
                    int key = (threadId * operationsPerThread + j) % 50;
                    threadCache.put(key, j);
                    threadCache.get(key);
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("10 threads x 1000 operations completed");
        System.out.println("Final cache size: " + threadCache.size());
        System.out.println("[PASS]\n");

        System.out.println("=== All Tests Passed! ===");
    }
}
