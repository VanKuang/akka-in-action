package cn.van.kuang.worker;

import akka.actor.UntypedActor;
import cn.van.kuang.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CompleteListener extends UntypedActor {

    private final static Logger logger = LoggerFactory.getLogger(CompleteListener.class);

    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof Response) {
            Response response = (Response) message;

            logger.info("Aggregate all response(s), size: {}", ((List) response.getResult()).size());
            logger.info("Going to shutdown akka system");

            getContext().system().terminate();
        } else {
            unhandled(message);
        }
    }
}
