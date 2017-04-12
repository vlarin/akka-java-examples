package org.royrusso.command;

import java.io.Serializable;

/**
 * Created by Vlad on 12.04.2017.
 */
public class ProcessDataCommand implements Serializable {
    private final int tempo;
    private final int total;

    public ProcessDataCommand(int tempo, int total) {
        this.tempo = tempo;
        this.total = total;
    }

    public int getTempo() {
        return tempo;
    }

    public int getTotal() {
        return total;
    }
}
