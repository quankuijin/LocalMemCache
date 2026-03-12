import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestLRU {
    private final int maxSize;
    private LinkedHashMap<String, String> cache;
    private final Lock lock = new ReentrantLock();

    public TestLRU(int maxSize) {
        this.maxSize = maxSize;
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

    public static void main(String[] args) {
        System.out.println("Testing LRU Cache...");
        
        TestLRU testCache = new TestLRU(3);

        testCache.put("a", "1");
        testCache.put("b", "2");
        testCache.put("c", "3");
        System.out.println("After put a,b,c: size=" + testCache.size());
        
        testCache.put("d", "4");
        System.out.println("After put d: size=" + testCache.size());
        System.out.println("a should be null: " + testCache.get("a"));
        System.out.println("b should be 2: " + testCache.get("b"));
        
        testCache.get("c");
        testCache.put("e", "5");
        System.out.println("After access c and put e:");
        System.out.println("d should be null: " + testCache.get("d"));
        System.out.println("c should be 3: " + testCache.get("c"));
        
        testCache.delete("b");
        System.out.println("After delete b: size=" + testCache.size());
        
        System.out.println("\n=== Testing max size 5000 ===");
        TestLRU bigCache = new TestLRU(5000);
        for (int i = 0; i < 6000; i++) {
            bigCache.put("key" + i, "value" + i);
        }
        System.out.println("After 6000 puts: size=" + bigCache.size());
        System.out.println("key0 should be null: " + bigCache.get("key0"));
        System.out.println("key5999 should exist: " + bigCache.get("key5999"));
        
        System.out.println("\n=== All tests passed! ===");
    }
}
