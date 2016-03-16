package cn.van.kuang.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.dispatch.Futures;
import akka.dispatch.Mapper;
import akka.pattern.Patterns;
import akka.util.Timeout;
import cn.van.kuang.model.Request;
import cn.van.kuang.model.Response;
import cn.van.kuang.model.Result;
import cn.van.kuang.model.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AskActor extends UntypedActor {

    private final static Logger logger = LoggerFactory.getLogger(AskActor.class);

    private final ActorRef queryWoker;

    public AskActor() {
        this.queryWoker = getContext().actorOf(Props.create(QueryActor.class));
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if ("ask".equals(message)) {

            logger.info("Going to get response synchronous...");

            final Timeout timeout = new Timeout(Duration.create(5, TimeUnit.SECONDS));

            final List<Future<Object>> futures = new ArrayList<Future<Object>>();

            futures.add(Patterns.ask(queryWoker, new Request(1), timeout));
            futures.add(Patterns.ask(queryWoker, new Request(2), timeout));

            final Future<Iterable<Object>> aggregated = Futures.sequence(futures, getContext().system().dispatcher());

            final Future<Response> finalFuture = aggregated.map(
                    new Mapper<Iterable<Object>, Response>() {
                        @Override
                        public Response apply(Iterable<Object> parameter) {
                            final Iterator<Object> iterator = parameter.iterator();

                            List<Result> responses = new ArrayList<Result>();
                            while (iterator.hasNext()) {
                                Object result = ((Response) iterator.next()).getResult();
                                responses.add((Result) result);
                            }

                            return new Response<List<Result>>(StatusCode.SUCCESS, responses);
                        }
                    },
                    getContext().system().dispatcher()
            );

            final ActorRef resultReceiver = getContext().actorOf(Props.create(ResultReceiver.class));
            Patterns.pipe(finalFuture, getContext().system().dispatcher()).to(resultReceiver);
        } else {
            unhandled(message);
        }
    }

}
