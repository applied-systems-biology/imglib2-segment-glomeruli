package org.hkijena.segment_glomeruli.tasks;

import org.hkijena.segment_glomeruli.DataInterface;

public class SegmentGlomeruli3D extends DAGTask {

    public SegmentGlomeruli3D(int tid, DataInterface dataInterface) {
        super(tid, dataInterface);
    }

    @Override
    public void work() {
        System.out.println("Running SegmentGlomeruli3D on " + getDataInterface().getInputData().toString());
    }
}
