package cn.van.kuang.worker;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinPool;
import cn.van.kuang.model.Request;
import cn.van.kuang.model.Response;
import cn.van.kuang.model.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MasterWorker extends UntypedActor {

    private final static Logger logger = LoggerFactory.getLogger(MasterWorker.class);

    private final ActorRef workerRouter;

    private final ActorRef listener;

    private List<Response> responses = new ArrayList<Response>();

    private int queryCount;

    public MasterWorker(ActorRef listener) {
        this.listener = listener;
        this.workerRouter = this.getContext().actorOf(
                new RoundRobinPool(5).props(Props.create(QueryWorker.class)),
                "QueryWorker");
    }

    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof String) {
            String queryString = (String) message;

            String[] strings = queryString.split(",");

            queryCount = strings.length;

            for (String id : strings) {
                workerRouter.tell(new Request(Integer.valueOf(id)), getSelf());
            }
        } else if (message instanceof Response) {

            logger.info("Got response: {}", message);

            responses.add((Response) message);

            if (responses.size() == queryCount) {
                listener.tell(new Response<List<Response>>(StatusCode.SUCCESS, responses), getSelf());
            }

        } else {
            unhandled(message);
        }
    }

}
