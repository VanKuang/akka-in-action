package cn.van.kuang.akka.persistence;

import akka.japi.Procedure;
import akka.persistence.SaveSnapshotFailure;
import akka.persistence.SaveSnapshotSuccess;
import akka.persistence.SnapshotOffer;
import akka.persistence.UntypedPersistentActor;
import cn.van.kuang.akka.util.ID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PersistentActor extends UntypedPersistentActor {

    private final static Logger logger = LoggerFactory.getLogger(PersistentActor.class);

    private StateHolder stateHolder;

    public PersistentActor() {
        this.stateHolder = new StateHolder();
    }

    @Override
    public void onReceiveRecover(Object message) throws Exception {
        if (message instanceof SnapshotOffer) {
            StateHolder snapshot = (StateHolder) ((SnapshotOffer) message).snapshot();

            logger.info("On recover, {}", snapshot);

            stateHolder = snapshot;
        } else if (message instanceof String) {
            stateHolder.onStateAdd((String) message);
        } else {
            unhandled(message);
        }
    }

    @Override
    public void onReceiveCommand(Object message) throws Exception {
        if ("print".equals(message)) {
            logger.info("Received message [{}]", message);
        } else if ("snapshot".equals(message)) {
            saveSnapshot(stateHolder.copy());
            logger.info("Saved snapshot.");
        } else if (message instanceof SaveSnapshotSuccess) {
            logger.info("Save snapshot successfully");
        } else if (message instanceof SaveSnapshotFailure) {
            logger.error("Fail to save snapshot", ((SaveSnapshotFailure) message).cause());
        } else if (message instanceof String) {
            String state = (String) message;
            persist(state, new Procedure<String>() {
                public void apply(String evt) throws Exception {
                    logger.info("Invoked StateHolder.onStateAdd({})", evt);

                    stateHolder.onStateAdd(evt);
                }
            });
        } else {
            unhandled(message);
        }
    }

    public String persistenceId() {
        return ID.generate();
    }

}
