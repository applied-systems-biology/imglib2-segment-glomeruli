package org.hkijena.segment_glomeruli.tasks;

import org.hkijena.segment_glomeruli.DataInterface;

public class QuantifyGlomeruli extends DAGTask {

    public QuantifyGlomeruli(Integer tid, DataInterface dataInterface) {
        super(tid, dataInterface);
    }

    @Override
    public void work() {
        System.out.println("Running QuantifyGlomeruli on " + getDataInterface().getInputData().toString());
    }
}
