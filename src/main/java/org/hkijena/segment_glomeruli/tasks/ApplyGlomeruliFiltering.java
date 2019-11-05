package org.hkijena.segment_glomeruli.tasks;

import org.hkijena.segment_glomeruli.DataInterface;

public class ApplyGlomeruliFiltering extends DAGTask {

    public ApplyGlomeruliFiltering(Integer tid, DataInterface dataInterface) {
        super(tid, dataInterface);
    }

    @Override
    public void work() {
        System.out.println("Running ApplyGlomeruliFiltering on " + getDataInterface().getInputData().toString());
    }
}
