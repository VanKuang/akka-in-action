package cn.van.kuang.actor;

import akka.actor.UntypedActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PoisonedActor extends UntypedActor {

    private final static Logger logger = LoggerFactory.getLogger(PoisonedActor.class);

    @Override
    public void postStop() throws Exception {
        logger.info("PoisonedActor is death...");
    }

    @Override
    public void onReceive(Object message) throws Exception {
        logger.info("Received: {}", message);
    }
}
