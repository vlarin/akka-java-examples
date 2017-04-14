package flow.prototype.messages;

import java.io.Serializable;

/**
 * Created by Pocomaxa on 4/14/2017.
 */
public class IncVarResult implements Serializable {

    private final boolean isSuccess;
    private final int result;

    public IncVarResult(boolean isSuccess, int result) {
        this.isSuccess = isSuccess;
        this.result = result;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public int getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "IncVarResult " + isSuccess + " " + result;
    }

}
