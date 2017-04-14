package flow.prototype.messages;

import java.io.Serializable;

/**
 * Created by Pocomaxa on 4/14/2017.
 */
public class IncVarCommand implements Serializable {

    private final int boundary;

    public IncVarCommand(int boundary) {
        this.boundary = boundary;
    }

    public int getBoundary() {
        return boundary;
    }

    @Override
    public String toString() {
        return "IncVarCommand " + boundary;
    }

}
