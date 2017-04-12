package org.royrusso.event;

import java.io.Serializable;

/**
 * Created by Vlad on 12.04.2017.
 */
public class ProcessElementEvent implements Serializable {
    private final String element;

    public ProcessElementEvent(String element) {
        this.element = element;
    }

    public String getElement() {
        return element;
    }
}
