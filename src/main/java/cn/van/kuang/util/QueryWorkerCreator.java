package cn.van.kuang.util;

import akka.japi.Creator;
import cn.van.kuang.worker.QueryWorker;

public class QueryWorkerCreator implements Creator<QueryWorker> {

    public QueryWorker create() throws Exception {
        return new QueryWorker();
    }

}
