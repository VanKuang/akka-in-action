package cn.van.kuang.util;

import akka.japi.Creator;
import cn.van.kuang.actor.QueryActor;

public class QueryWorkerCreator implements Creator<QueryActor> {

    public QueryActor create() throws Exception {
        return new QueryActor();
    }

}
