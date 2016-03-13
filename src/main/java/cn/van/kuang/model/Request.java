package cn.van.kuang.model;

import cn.van.kuang.util.ID;

public class Request {

    private final int queryID;
    private final String id;
    private final long timestamp;

    public Request(int queryID) {
        this.queryID = queryID;

        this.id = ID.generateRequestID();
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
                ", timestamp=" + timestamp +
                '}';
    }
}