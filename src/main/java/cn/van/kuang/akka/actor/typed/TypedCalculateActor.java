package cn.van.kuang.akka.actor.typed;

import akka.actor.ActorSystem;
import akka.actor.TypedActor;
import akka.actor.TypedProps;
import akka.japi.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

public class TypedCalculateActor {

    private final static Logger logger = LoggerFactory.getLogger(TypedCalculateActor.class);

    private final ActorSystem actorSystem;

    public TypedCalculateActor(ActorSystem actorSystem) {
        this.actorSystem = actorSystem;
    }

    public void testAPIs() throws Exception {
        Calculator calculator = TypedActor.get(actorSystem).typedActorOf(
                new TypedProps<SimpleCalculator>(Calculator.class, SimpleCalculator.class)
        );

        calculator.multiplyAndPrint(8, 8);

        Future<Integer> resultFuture = calculator.multiplyAsynchronous(8, 8);
        int asyncResult = Await.result(resultFuture, Duration.create(3, TimeUnit.SECONDS));
        logger.info("Got asyncResult asynchronous, [{}]", asyncResult);

        Option<Integer> option = calculator.multiplySynchronous(8, 8);
        Integer optionResult = option.get();
        logger.info("Got asyncResult synchronous, [{}]", optionResult);

        int result = calculator.multiply(8, 8);
        logger.info("Got result in JAVA way, [{}]", result);

        TypedActor.get(actorSystem).poisonPill(calculator);
    }
}
