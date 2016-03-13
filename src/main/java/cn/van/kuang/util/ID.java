package cn.van.kuang.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Simple ID generator base on current system time
 */
public final class ID {

    private final static SimpleDateFormat formater = new SimpleDateFormat("yyyymmddHHMMSSs");

    public static String generateRequestID() {
        return formater.format(new Date());
    }

    private ID() {
    }

}
