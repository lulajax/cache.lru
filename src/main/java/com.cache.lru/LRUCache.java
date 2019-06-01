package com.cache.lru;

import java.util.HashMap;
import java.util.Map;

public class LRUCache<K, V> {
    public static void main(String[] args) {
        LRUCache<Integer, Integer> lruCache = new LRUCache<>(5);
        lruCache.set(1, 1);
        lruCache.set(2, 2);
        lruCache.set(3, 3);
        lruCache.set(4, 4);
        lruCache.set(5, 5);
        lruCache.set(6, 6);

        lruCache.get(1);
    }

    int capacity;
    Node<K, V> head;
    Node<K, V> tail;
    Map<K, Node<K, V>> cachesMap;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        cachesMap = new HashMap<>(capacity);
    }

    void set(K key, V val) {
        Node<K, V> node = cachesMap.get(key);
        // 缓存中已经存在,移动到链表头部
        if (node != null) {
            moveToHead(node);
        } else {
            add(key, val);
        }

    }

    V get(K key) {
        Node<K, V> node = cachesMap.get(key);
        if (node != null) {
            moveToHead(node);
            return node.val;
        } else {
            return null;
        }
    }

    void add(K key, V val) {
        Node node = new Node<>(key, val, head);
        if (cachesMap.size() >= capacity) {
            // 移除最久未使用node
            tail.pre.next = null;
            tail.pre = null;
            cachesMap.remove(tail.key);
        }

        if (head != null) {
            node.next = head;
            head.pre = node;
        }
        head = node;

        if (tail == null) {
            tail = node;
        }

        cachesMap.put(key, node);
    }

    void moveToHead(Node<K, V> node) {
        if (head != null) {
            // 不是头结点
            if (node.pre != null) {
                node.pre.next = node.next;
            }
            // 不是尾节点
            if (node.next != null) {
                node.next.pre = node.pre;
            } else {
                tail = node.pre;
            }
            // 移动到链表头部
            node.next = head;
            head.pre = node;
        }
        head = node;
    }

    class Node<K, V> {
        final K key;
        volatile V val;
        volatile Node<K, V> next;
        volatile Node<K, V> pre;

        Node(K key, V val) {
            this.key = key;
            this.val = val;
        }

        Node(K key, V val, Node<K, V> next) {
            this(key, val);
            this.next = next;
        }

        Node(K key, V val, Node<K, V> next, Node<K, V> pre) {
            this(key, val);
            this.next = next;
            this.pre = pre;
        }
    }

}
