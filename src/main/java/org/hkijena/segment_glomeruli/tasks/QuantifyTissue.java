package org.hkijena.segment_glomeruli.tasks;

import org.hkijena.segment_glomeruli.DataInterface;

public class QuantifyTissue extends DAGTask {

    public QuantifyTissue(Integer tid, DataInterface dataInterface) {
        super(tid, dataInterface);
    }

    @Override
    public void work() {
        System.out.println("Running QuantifyTissue on " + getDataInterface().getInputData().toString());
    }
}
