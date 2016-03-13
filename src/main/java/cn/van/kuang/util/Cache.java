package cn.van.kuang.util;

import cn.van.kuang.model.Result;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class Cache {

    private final static Map<Integer, Result> cache = new ConcurrentHashMap<Integer, Result>();

    static {
        cache.put(1, new Result(1, "GOOGLE"));
        cache.put(2, new Result(2, "APPLE"));
        cache.put(3, new Result(3, "MICROSOFT"));
        cache.put(4, new Result(4, "IBM"));
        cache.put(5, new Result(5, "FACEBOOK"));
    }

    public static Result getResult(int id) {
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
