package cn.van.kuang;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import cn.van.kuang.actor.ActorMaster;
import cn.van.kuang.actor.AskActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Starting test akka system.");

        ActorSystem akkaSystem = ActorSystem.create("Hello-World-In-Akka");

        final ActorRef master = akkaSystem.actorOf(Props.create(ActorMaster.class), "ActorMaster");

        master.tell("1,2,3,4,5,100", ActorRef.noSender());

        testSelection(akkaSystem);

        final ActorRef askActor = akkaSystem.actorOf(Props.create(AskActor.class), "AskActor");
        askActor.tell("ask", ActorRef.noSender());
    }

    private static void testSelection(ActorSystem akkaSystem) {
        ActorSelection masterWorkerSelection = akkaSystem.actorSelection("/user/ActorMaster");
        logger.info("Selection of master actor: {}", masterWorkerSelection);
    }

}
