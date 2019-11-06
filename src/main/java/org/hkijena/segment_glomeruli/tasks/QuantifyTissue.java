package org.hkijena.segment_glomeruli.tasks;

import org.hkijena.segment_glomeruli.DataInterface;
import org.hkijena.segment_glomeruli.data.TissueQuantificationResult;

public class QuantifyTissue extends DAGTask {

    public QuantifyTissue(Integer tid, DataInterface dataInterface) {
        super(tid, dataInterface);
    }

    @Override
    public void work() {
        System.out.println("Running QuantifyTissue on " + getDataInterface().getInputData().toString());

        TissueQuantificationResult result = getDataInterface().getTissueQuantificationResult();
        result.numPixels = getDataInterface().getTissuePixelCount();
        result.volumeMicrons3 = result.numPixels * getDataInterface().getVoxelSizeXY() * getDataInterface().getVoxelSizeXY() * getDataInterface().getVoxelSizeZ();
    }
}
