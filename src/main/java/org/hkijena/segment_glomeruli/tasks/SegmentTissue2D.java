package org.hkijena.segment_glomeruli.tasks;

import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.FloatType;
import org.hkijena.segment_glomeruli.DataInterface;
import org.hkijena.segment_glomeruli.Filters;

public class SegmentTissue2D extends DAGTask {

    private long planeZIndex;

    private int medianFilterSize = 3;
    private double downscaleFactor = 10;
    private double thresholdingPercentile = 40;
    private double thresholdingPercentileFactor = 1.5;
    private int morphDiskRadius = 5;
    private double labelMinPercentile = 2;

    public SegmentTissue2D(Integer tid, DataInterface dataInterface, long planeZIndex) {
        super(tid, dataInterface);
        this.planeZIndex = planeZIndex;
    }

    @Override
    public void work() {
        System.out.println("Running SegmentTissue2D on " + getDataInterface().getInputData().toString() + " z=" + planeZIndex);
        final Img<UnsignedByteType> importedImage = getDataInterface().getInputData().getPlane(planeZIndex);

        Img<FloatType> img = ImageJFunctions.convertFloat(ImageJFunctions.wrap(importedImage, "img"));
        Filters.median(img.copy(), img, medianFilterSize, new FloatType(0));
        Filters.normalizeByMax(img);

        final double xsize = getDataInterface().getVoxelSizeXY();
        final double gaussSigma = 3.0 / xsize;

        ImageJFunctions.show(img);

        System.out.println();
        
    }
}
