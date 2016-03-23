package cn.van.kuang.akka.actor.typed;

import akka.japi.Option;
import scala.concurrent.Future;

public interface Calculator {

    void multiplyAndPrint(int a, int b);

    Future<Integer> multiplyAsynchronous(int a, int b);

    Option<Integer> multiplySynchronous(int a, int b);

    int multiply(int a, int b);

}
