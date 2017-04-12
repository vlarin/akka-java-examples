package flow.prototype;

/**
 * Created by Vlad on 12.04.2017.
 */
public class NearReference<T> {
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
