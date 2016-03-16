package cn.van.kuang.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WatchActor extends UntypedActor {

    private final static Logger logger = LoggerFactory.getLogger(WatchActor.class);

    private final ActorRef child;
    private ActorRef lastSender;

    public WatchActor() {
        this.child = getContext().actorOf(Props.empty(), "child");
        this.lastSender = getContext().system().deadLetters();
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();

        logger.info("preStart() of WatchActor is invoked");

        getContext().watch(this.child);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if ("kill".equals(message)) {
            logger.info("Received 'kill' message");

            getContext().stop(child);
            lastSender = getSender();
        } else if (message instanceof Terminated) {
            logger.info("Received terminated message, {}", message);

            final Terminated t = (Terminated) message;
            if (t.getActor() == child) {
                lastSender.tell("finished", getSelf());
            }
        } else {
            unhandled(message);
        }
    }
}
