package flow.prototype;

import akka.actor.Actor;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;

/**
 * Created by Vlad on 12.04.2017.
 */
public class SyncVar<T> extends UntypedActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public static <K> Props create(K initial) {
        return Props.create(new Creator<SyncVar<K>>() {
            @Override
            public SyncVar<K> create() throws Exception {
                return null;
            }
        });
    }

    private NearReference variable;

    @Override
    public void onReceive(Object message) throws Exception {

    }
}
