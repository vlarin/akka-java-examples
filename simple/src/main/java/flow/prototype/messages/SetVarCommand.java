package flow.prototype.messages;

import java.io.Serializable;

/**
 * Created by Pocomaxa on 4/14/2017.
 */
public class SetVarCommand<T extends Serializable> implements Serializable {

    private T value;

    public SetVarCommand(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "SetVarCommand " + value.toString();
    }

}
