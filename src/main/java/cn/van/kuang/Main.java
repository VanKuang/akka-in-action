package cn.van.kuang;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import cn.van.kuang.worker.MasterWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Starting test akka system.");

        ActorSystem akkaSystem = ActorSystem.create("Hello-World-In-Akka");

        final ActorRef master = akkaSystem.actorOf(Props.create(MasterWorker.class), "MasterWorker");

        master.tell("1,2,3,4,5,100", ActorRef.noSender());
    }

}
