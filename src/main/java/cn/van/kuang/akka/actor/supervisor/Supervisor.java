package cn.van.kuang.akka.actor.supervisor;

import akka.actor.OneForOneStrategy;
import akka.actor.SupervisorStrategy;
import akka.actor.UntypedActor;
import akka.japi.Function;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

import static akka.actor.SupervisorStrategy.*;

public class Supervisor extends UntypedActor {

    private final SupervisorStrategy supervisorStrategy = new OneForOneStrategy(10,
            Duration.create(1, TimeUnit.MINUTES),
            new Function<Throwable, SupervisorStrategy.Directive>() {
                public SupervisorStrategy.Directive apply(Throwable throwable) throws Exception {
                    if (throwable instanceof ArithmeticException) {
                        return resume();
                    } else if (throwable instanceof NullPointerException) {
                        return restart();
                    } else if (throwable instanceof IllegalArgumentException) {
                        return stop();
                    } else {
                        return escalate();
                    }
                }
            }
    );

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return supervisorStrategy;
    }

    @Override
    public void onReceive(Object message) throws Exception {

    }
}
