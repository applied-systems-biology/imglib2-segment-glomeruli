package org.hkijena.segment_glomeruli.tasks;

import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import org.hkijena.segment_glomeruli.DataInterface;

public class SegmentGlomeruli2D extends DAGTask {
    private long planeZIndex;

    public SegmentGlomeruli2D(Integer tid, DataInterface dataInterface, long planeZIndex) {
        super(tid, dataInterface);
        this.planeZIndex = planeZIndex;
    }

    @Override
    public void work() {
        System.out.println("Running SegmentGlomeruli2D on " + getDataInterface().getInputData().toString() + " z=" + planeZIndex);
        Img<UnsignedByteType> importedImage = getDataInterface().getInputData().getPlane(planeZIndex);
    }
}
