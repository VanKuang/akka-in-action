package cn.van.kuang.akka.actor;

import akka.actor.UntypedActor;
import cn.van.kuang.akka.model.Request;
import cn.van.kuang.akka.model.Response;
import cn.van.kuang.akka.model.Result;
import cn.van.kuang.akka.model.StatusCode;
import cn.van.kuang.akka.util.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryActor extends UntypedActor {

    private final static Logger logger = LoggerFactory.getLogger(QueryActor.class);

    @Override
    public void preStart() throws Exception {
        Cache.init();
    }

    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof Request) {
            Request request = (Request) message;

            logger.info("Received {} from {}", request, getSender().path().name());

            Result result = Cache.getResult(request.getQueryID());

            Response response;
            if (result == null) {
                response = new Response<Exception>(StatusCode.FAILURE, new RuntimeException("No such result"));
            } else {
                response = new Response<Result>(StatusCode.SUCCESS, result);
            }

            getSender().tell(response, getSelf());
        } else {
            unhandled(message);
        }
    }

}
