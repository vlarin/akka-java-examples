package flow.prototype;

import java.io.Serializable;

/**
 * Created by Vlad on 12.04.2017.
 */
public class NearReference<T extends Serializable> implements Serializable {
    private T value;

    public NearReference(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
