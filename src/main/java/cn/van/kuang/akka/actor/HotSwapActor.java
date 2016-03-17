package cn.van.kuang.akka.actor;

import akka.actor.UntypedActor;
import akka.japi.Procedure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HotSwapActor extends UntypedActor {

    private final static Logger logger = LoggerFactory.getLogger(HotSwapActor.class);

    private Procedure<Object> basketballProcedure = new Procedure<Object>() {
        public void apply(Object message) {
            if ("Football".equals(message)) {
                logger.info("Become football mode");
                getContext().become(footballProcedure);
            } else {
                logger.info("In basketball mode");
            }
        }
    };

    private Procedure<Object> footballProcedure = new Procedure<Object>() {
        public void apply(Object message) {
            if ("Basketball".equals(message)) {
                logger.info("Become basketball mode");
                getContext().become(basketballProcedure);
            } else {
                logger.info("In football mode");
            }
        }
    };

    @Override
    public void onReceive(Object message) throws Exception {
        if ("Basketball".equals(message)) {
            logger.info("Become basketball mode");
            getContext().become(basketballProcedure);
        } else {
            unhandled(message);
        }
    }

}
