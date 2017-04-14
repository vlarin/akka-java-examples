package flow.prototype;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;
import flow.prototype.messages.*;
import scala.collection.Set;

import java.io.Serializable;

/**
 * Created by Vlad on 12.04.2017.
 */
public class SyncVar<T extends Serializable> extends UntypedActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public static <K extends Serializable> Props create(final NearReference<K> initial) {
        return Props.create(new Creator<SyncVar<K>>() {
            @Override
            public SyncVar<K> create() throws Exception {
                return new SyncVar<>(initial);
            }
        });
    }

    protected final NearReference<T> variable;

    public SyncVar(NearReference<T> initial) {
        variable = initial;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof GetVarCommand) {
            context().sender().tell(new GetVarResult<>(variable.getValue()), self());
        } else if (message instanceof SetVarCommand) {
            try {
                SetVarCommand<T> cmd = (SetVarCommand<T>) message;
                variable.setValue(cmd.getValue());
            } catch (ClassCastException ex) {
                log.error("Invalid value type, unable to set!");
                unhandled(message);
            }
        } else {
            unhandled(message);
        }
    }
}
