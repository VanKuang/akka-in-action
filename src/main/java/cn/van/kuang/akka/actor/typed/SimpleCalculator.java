package cn.van.kuang.akka.actor.typed;

import akka.dispatch.Futures;
import akka.japi.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.Future;

public class SimpleCalculator implements Calculator {

    private final static Logger logger = LoggerFactory.getLogger(SimpleCalculator.class);

    public void multiplyAndPrint(int a, int b) {
        logger.info("Calculate quite: {} x {} = {}", a, b, a * b);
    }

    public Future<Integer> multiplyAsynchronous(int a, int b) {
        logger.info("Calculate async");
        return Futures.successful(a * b);
    }

    public Option<Integer> multiplySynchronous(int a, int b) {
        logger.info("Calculate sync");
        return Option.some(a * b);
    }

    public int multiply(int a, int b) {
        logger.info("Calculate in JAVA");
        return a * b;
    }
}
