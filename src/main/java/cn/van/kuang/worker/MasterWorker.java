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

    private final static int MAX_QUERY_WORKER_POOL = 5;

    private final ActorRef workerRouter;

    private final ActorRef listener;

    private final ActorRef watchActor;

    private List<Response> responses = new ArrayList<Response>();

    private int queryCount;

    public MasterWorker() {
        RoundRobinPool roundRobinPool = new RoundRobinPool(MAX_QUERY_WORKER_POOL);

        // create it's child actor
        this.listener = getContext().actorOf(
                Props.create(CompleteListener.class),
                "CompleteListener");
        this.workerRouter = getContext().actorOf(
                roundRobinPool.props(Props.create(QueryWorker.class)),
                "QueryWorker");
        this.watchActor = getContext().actorOf(
                Props.create(WatchActor.class),
                "WatchActor");
    }

    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof String) {
            if ("finished".equals(message)) {
                logger.info("Received message from WatchActor, it has already terminated");
                return;
            }

            dispatchRequestToChildWorkers((String) message);
        } else if (message instanceof Response) {

            logger.info("Got response: {}", message);

            responses.add((Response) message);

            if (responses.size() == 1) {
                watchActor.tell("kill", getSelf());
            }

            if (responses.size() == queryCount) {
                listener.tell(new Response<List<Response>>(StatusCode.SUCCESS, responses), getSelf());
            }
        } else {
            unhandled(message);
        }
    }

    private void dispatchRequestToChildWorkers(String queryString) {
        String[] requestIDs = queryString.split(",");

        queryCount = requestIDs.length;

        for (String id : requestIDs) {
            workerRouter.tell(new Request(Integer.valueOf(id)), getSelf());
        }
    }

}
