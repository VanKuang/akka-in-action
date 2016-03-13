package cn.van.kuang.worker;

import akka.actor.UntypedActor;
import cn.van.kuang.model.Request;
import cn.van.kuang.model.Response;
import cn.van.kuang.model.Result;
import cn.van.kuang.model.StatusCode;
import cn.van.kuang.util.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryWorker extends UntypedActor {

    private final static Logger logger = LoggerFactory.getLogger(QueryWorker.class);

    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof Request) {
            Request request = (Request) message;

            logger.info("Received {}", request);

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
