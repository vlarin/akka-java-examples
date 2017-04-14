package flow.prototype;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;
import flow.prototype.messages.*;

import java.io.Serializable;

/**
 * Created by Vlad on 12.04.2017.
 */
public class CounterVar extends SyncVar<Integer> {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public static <T extends Serializable> Props create(final NearReference<T> initial) {
        return Props.create(new Creator<CounterVar>() {
            @Override
            public CounterVar create() throws Exception {
                return new CounterVar((NearReference<Integer>)initial);
            }
        });
    }

    public CounterVar(NearReference<Integer> initial) {
        super(initial);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof IncVarCommand) {
            IncVarCommand cmd = (IncVarCommand)message;
            if (variable.getValue() < cmd.getBoundary()) {
                variable.setValue(variable.getValue() + 1);
                context().sender().tell(new IncVarResult(true, variable.getValue()), self());
            } else {
                context().sender().tell(new IncVarResult(false, variable.getValue()), self());
            }
        } else {
            super.onReceive(message);
        }
    }
}
