package cn.van.kuang.akka.util;

import akka.japi.Creator;
import cn.van.kuang.akka.actor.QueryActor;

public class QueryWorkerCreator implements Creator<QueryActor> {

    public QueryActor create() throws Exception {
        return new QueryActor();
    }

}
