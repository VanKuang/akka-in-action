package cn.van.kuang.akka.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Simple ID generator base on machine name and millis time
 */
public final class ID {

    private static long SEED = System.currentTimeMillis();

    public synchronized static String generateRequestID() {
        return getMachineName() + "-" + (SEED++);
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
