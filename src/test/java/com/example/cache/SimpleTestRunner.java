package com.example.cache;

import com.example.cache.config.CacheProperties;
import com.example.cache.service.MemoryCacheService;

public class SimpleTestRunner {

    public static void main(String[] args) {
        int passed = 0;
        int failed = 0;

        CacheProperties properties = new CacheProperties();
        properties.setMaxSize(3);
        MemoryCacheService cache = new MemoryCacheService(properties);

        System.out.println("=== 开始测试 MemoryCacheService ===\n");

        System.out.println("测试1: put 和 get");
        cache.put("key1", "value1");
        if ("value1".equals(cache.get("key1"))) {
            System.out.println("  ✓ 通过");
            passed++;
        } else {
            System.out.println("  ✗ 失败");
            failed++;
        }

        System.out.println("\n测试2: get 不存在的 key");
        if (cache.get("nonexistent") == null) {
            System.out.println("  ✓ 通过");
            passed++;
        } else {
            System.out.println("  ✗ 失败");
            failed++;
        }

        System.out.println("\n测试3: delete");
        cache.put("key2", "value2");
        if (cache.delete("key2") && cache.get("key2") == null) {
            System.out.println("  ✓ 通过");
            passed++;
        } else {
            System.out.println("  ✗ 失败");
            failed++;
        }

        System.out.println("\n测试4: delete 不存在的 key");
        if (!cache.delete("nonexistent")) {
            System.out.println("  ✓ 通过");
            passed++;
        } else {
            System.out.println("  ✗ 失败");
            failed++;
        }

        System.out.println("\n测试5: LRU 淘汰机制");
        cache.put("a", "1");
        cache.put("b", "2");
        cache.put("c", "3");
        cache.get("a");
        cache.put("d", "4");
        if (cache.get("b") == null && 
            "1".equals(cache.get("a")) && 
            "3".equals(cache.get("c")) && 
            "4".equals(cache.get("d"))) {
            System.out.println("  ✓ 通过");
            passed++;
        } else {
            System.out.println("  ✗ 失败");
            failed++;
        }

        System.out.println("\n测试6: size");
        if (cache.size() == 3) {
            System.out.println("  ✓ 通过");
            passed++;
        } else {
            System.out.println("  ✗ 失败: size=" + cache.size());
            failed++;
        }

        System.out.println("\n测试7: 更新已存在的 key");
        cache.put("x", "old");
        cache.put("x", "new");
        if ("new".equals(cache.get("x"))) {
            System.out.println("  ✓ 通过");
            passed++;
        } else {
            System.out.println("  ✗ 失败");
            failed++;
        }

        System.out.println("\n=== 测试结果 ===");
        System.out.println("通过: " + passed);
        System.out.println("失败: " + failed);
        System.out.println("总计: " + (passed + failed));

        if (failed > 0) {
            System.exit(1);
        }
    }
}
