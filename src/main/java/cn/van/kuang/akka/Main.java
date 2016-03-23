package cn.van.kuang.akka;

import akka.actor.*;
import akka.pattern.Patterns;
import cn.van.kuang.akka.actor.ActorMaster;
import cn.van.kuang.akka.actor.AskActor;
import cn.van.kuang.akka.actor.HotSwapActor;
import cn.van.kuang.akka.actor.PoisonedActor;
import cn.van.kuang.akka.actor.typed.TypedCalculateActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

public class Main {

    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        logger.info("Starting test akka system.");

        ActorSystem akkaSystem = ActorSystem.create("Hello-World-In-Akka");

        final ActorRef master = akkaSystem.actorOf(Props.create(ActorMaster.class), "ActorMaster");

        master.tell("1,2,3,4,5,100", ActorRef.noSender());

        trySelection(akkaSystem);

        tryAsk(akkaSystem);

        tryPoison(akkaSystem);

        tryGracefulStop(akkaSystem);

        tryHotSwap(akkaSystem);

        tryTypedActor(akkaSystem);
    }

    private static void trySelection(ActorSystem akkaSystem) {
        ActorSelection masterWorkerSelection = akkaSystem.actorSelection("/user/ActorMaster");
        logger.info("Selection of master actor: {}", masterWorkerSelection);
    }

    private static void tryAsk(ActorSystem akkaSystem) {
        final ActorRef askActor = akkaSystem.actorOf(Props.create(AskActor.class), "AskActor");
        askActor.tell("ask", ActorRef.noSender());
    }

    private static void tryPoison(ActorSystem akkaSystem) {
        final ActorRef poisonedActor = akkaSystem.actorOf(Props.create(PoisonedActor.class), "PoisonedActor");
        poisonedActor.tell(PoisonPill.getInstance(), ActorRef.noSender());
    }

    private static void tryGracefulStop(ActorSystem akkaSystem) {
        final ActorRef oneShotActor = akkaSystem.actorOf(Props.create(PoisonedActor.class), "OneShotActor");

        Future<Boolean> future = Patterns.gracefulStop(oneShotActor, Duration.create(1, TimeUnit.SECONDS), "Shutdown");
        oneShotActor.tell(PoisonPill.getInstance(), ActorRef.noSender());

        try {
            Await.result(future, Duration.create(2, TimeUnit.SECONDS));
        } catch (Exception e) {
            logger.info("OneShotActor did not shutdown.", e);
        }
    }

    private static void tryHotSwap(ActorSystem akkaSystem) {
        final ActorRef hotSwapActor = akkaSystem.actorOf(Props.create(HotSwapActor.class), "HotSwapActor");
        hotSwapActor.tell("Basketball", ActorRef.noSender());
        hotSwapActor.tell("What now?", ActorRef.noSender());
        hotSwapActor.tell("Football", ActorRef.noSender());
        hotSwapActor.tell("What now?", ActorRef.noSender());
        hotSwapActor.tell("Basketball", ActorRef.noSender());
        hotSwapActor.tell("What now?", ActorRef.noSender());
    }

    private static void tryTypedActor(ActorSystem akkaSystem) throws Exception {
        TypedCalculateActor calculateActor = new TypedCalculateActor(akkaSystem);
        calculateActor.testAPIs();
    }

}
