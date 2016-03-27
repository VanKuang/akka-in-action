package cn.van.kuang.akka.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StateHolder implements Serializable {

    private final List<String> states;

    public StateHolder() {
        this(new ArrayList<String>());
    }

    public StateHolder(List<String> states) {
        this.states = states;
    }

    public void onStateAdd(String state) {
        states.add(state);
    }

    public StateHolder copy() {
        return new StateHolder(new ArrayList<String>(states));
    }

    @Override
    public String toString() {
        return "StateHolder{" +
                "states=" + states +
                '}';
    }
}
