package cn.van.kuang.akka.util;

import cn.van.kuang.akka.model.Result;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public final class Cache {

    private final static Map<Integer, Result> cache = new ConcurrentHashMap<Integer, Result>();

    private static AtomicBoolean isInitialized = new AtomicBoolean(false);

    public static void init() {
        cache.put(1, new Result(1, "GOOGLE"));
        cache.put(2, new Result(2, "APPLE"));
        cache.put(3, new Result(3, "MICROSOFT"));
        cache.put(4, new Result(4, "IBM"));
        cache.put(5, new Result(5, "FACEBOOK"));

        isInitialized.set(true);
    }

    public static Result getResult(int id) {
        if (!isInitialized.get()) {
            throw new RuntimeException("Cache not initialize yet");
        }

        // Mock latency query
        try {
            Thread.sleep(500L);
        } catch (InterruptedException ignore) {
        }

        return cache.get(id);
    }

    private Cache() {
    }

}
