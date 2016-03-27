package cn.van.kuang.akka.model;

import cn.van.kuang.akka.util.ID;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Request {

    private final int queryID;
    private final String id;
    private final long timestamp;

    private final static SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");

    public Request(int queryID) {
        this.queryID = queryID;

        this.id = ID.generate();
        this.timestamp = System.currentTimeMillis();
    }

    public int getQueryID() {
        return queryID;
    }

    public String getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Request{" +
                "queryID=" + queryID +
                ", id='" + id + '\'' +
                ", timestamp=" + formater.format(new Date(timestamp)) +
                '}';
    }
}