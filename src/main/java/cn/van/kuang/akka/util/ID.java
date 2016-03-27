package cn.van.kuang.akka.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Simple ID generator base on machine name and millis time
 */
public final class ID {

    private static Lock lock = new ReentrantLock();
    private static long SEED = System.currentTimeMillis();

    public static String generate() {
        long temp = 0;
        try {
            lock.lock();
            temp = SEED++;
        } finally {
            lock.unlock();
        }

        return getMachineName() + "-" + temp;
    }

    private static String getMachineName() {
        try {
            return InetAddress.getLocalHost().getHostName().toUpperCase();
        } catch (UnknownHostException e) {
            return "UNKNOWN";
        }
    }

    private ID() {
    }

}
