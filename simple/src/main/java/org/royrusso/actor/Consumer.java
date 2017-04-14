package org.royrusso.actor;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.royrusso.event.ProcessElementEvent;

/**
 * Created by Vlad on 12.04.2017.
 */
public class Consumer extends UntypedActor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof ProcessElementEvent) {
            log.info("Received - " + ((ProcessElementEvent) message).getElement());
        } else {
            unhandled(message);
        }
    }
}
