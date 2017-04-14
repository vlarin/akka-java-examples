package flow.prototype.messages;

import java.io.Serializable;

/**
 * Created by Pocomaxa on 4/14/2017.
 */
public class GetVarResult<T extends Serializable> implements Serializable {

    private T value;

    public GetVarResult(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
    @Override
    public String toString() {
        return "GetVarCommand";
    }

}
