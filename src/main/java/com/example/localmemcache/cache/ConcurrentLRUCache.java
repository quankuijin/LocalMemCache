package com.example.localmemcache.cache;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class ConcurrentLRUCache implements InitializingBean {

    @Value("${cache.max.size:5000}")
    private int maxSize;

    private Map<String, Node> cache;
    private Node head;
    private Node tail;
    private final Lock lock = new ReentrantLock();

    private static class Node {
        String key;
        String value;
        Node prev;
        Node next;
    }

    @Override
    public void afterPropertiesSet() {
        cache = new ConcurrentHashMap<>(maxSize);
        head = new Node();
        tail = new Node();
        head.next = tail;
        tail.prev = head;
    }

    public String get(String key) {
        Node node = cache.get(key);
        if (node == null) {
            return null;
        }
        moveToFront(node);
        return node.value;
    }

    public void put(String key, String value) {
        Node node = cache.get(key);
        if (node != null) {
            node.value = value;
            moveToFront(node);
            return;
        }

        lock.lock();
        try {
            if (cache.size() >= maxSize) {
                Node evicted = removeTail();
                if (evicted != null) {
                    cache.remove(evicted.key);
                }
            }
            Node newNode = new Node();
            newNode.key = key;
            newNode.value = value;
            cache.put(key, newNode);
            addToFront(newNode);
        } finally {
            lock.unlock();
        }
    }

    public void delete(String key) {
        Node node = cache.remove(key);
        if (node != null) {
            removeNode(node);
        }
    }

    public int size() {
        return cache.size();
    }

    private void moveToFront(Node node) {
        removeNode(node);
        addToFront(node);
    }

    private void addToFront(Node node) {
        lock.lock();
        try {
            node.prev = head;
            node.next = head.next;
            head.next.prev = node;
            head.next = node;
        } finally {
            lock.unlock();
        }
    }

    private void removeNode(Node node) {
        lock.lock();
        try {
            Node prev = node.prev;
            Node next = node.next;
            if (prev != null) prev.next = next;
            if (next != null) next.prev = prev;
        } finally {
            lock.unlock();
        }
    }

    private Node removeTail() {
        Node res = tail.prev;
        if (res == head) {
            return null;
        }
        removeNode(res);
        return res;
    }
}
