package org.hkijena.segment_glomeruli.tasks;

import org.hkijena.segment_glomeruli.DataInterface;

public class InitializeDataInterface extends DAGTask {

    public InitializeDataInterface(Integer tid, DataInterface dataInterface) {
        super(tid, dataInterface);
    }

    @Override
    public void work() {
        getDataInterface().initialize();
    }
}
