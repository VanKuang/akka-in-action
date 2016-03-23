package cn.van.kuang.akka.actor;

import akka.actor.*;
import akka.routing.RoundRobinPool;
import cn.van.kuang.akka.model.Request;
import cn.van.kuang.akka.model.Response;
import cn.van.kuang.akka.model.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.duration.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ActorMaster extends UntypedActor {

    private final static Logger logger = LoggerFactory.getLogger(ActorMaster.class);

    private final static int MAX_QUERY_WORKER_POOL = 5;

    private final ActorRef workerRouter;

    private final ActorRef listener;

    private final ActorRef watchActor;

    private List<Response> responses = new ArrayList<Response>();

    private int queryCount;

    public ActorMaster() {

        // create it's child actor
        this.listener = getContext().actorOf(
                Props.create(CompleteListener.class),
                "CompleteListener");
        this.watchActor = getContext().actorOf(
                Props.create(WatchActor.class),
                "WatchActor");

        final SupervisorStrategy strategy = new OneForOneStrategy(5,
                Duration.create(1, TimeUnit.MINUTES),
                Collections.<Class<? extends Throwable>>singletonList(Exception.class));
        final RoundRobinPool roundRobinPool = new RoundRobinPool(MAX_QUERY_WORKER_POOL);
        this.workerRouter = getContext().actorOf(
                roundRobinPool
                        .withSupervisorStrategy(strategy)
                        .props(Props.create(QueryActor.class)),
                "QueryActor");
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

            Response response = (Response) message;

            responses.add(response);

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
