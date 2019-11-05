package org.hkijena.segment_glomeruli;

import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.integer.UnsignedIntType;
import org.hkijena.segment_glomeruli.caches.OMETIFFImageCache;
import org.hkijena.segment_glomeruli.caches.TIFFPlanesImageCache;

import java.io.IOException;
import java.nio.file.Path;

public class DataInterface {
    private OMETIFFImageCache<UnsignedByteType> inputData;
    private TIFFPlanesImageCache<UnsignedByteType> tissueOutputData;
    private TIFFPlanesImageCache<UnsignedByteType> glomeruli2DOutputData;
    private TIFFPlanesImageCache<UnsignedIntType> glomeruli3DOutputData;

    public DataInterface(Path inputImageFile, Path outputDirectory) {
        try {
            Utils.ensureDirectory(outputDirectory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.inputData = new OMETIFFImageCache<>(inputImageFile, new UnsignedByteType());
        this.tissueOutputData = new TIFFPlanesImageCache<>(outputDirectory.resolve("tissue"), new UnsignedByteType(), this.inputData.getXSize(), this.inputData.getYSize());
        this.glomeruli2DOutputData = new TIFFPlanesImageCache<>(outputDirectory.resolve("glomeruli2d"), new UnsignedByteType(), this.inputData.getXSize(), this.inputData.getYSize());
        this.glomeruli3DOutputData = new TIFFPlanesImageCache<>(outputDirectory.resolve("glomeruli3d"), new UnsignedIntType(), this.inputData.getXSize(), this.inputData.getYSize());
    }

    public OMETIFFImageCache<UnsignedByteType> getInputData() {
        return inputData;
    }

    public TIFFPlanesImageCache<UnsignedByteType> getTissueOutputData() {
        return tissueOutputData;
    }

    public TIFFPlanesImageCache<UnsignedByteType> getGlomeruli2DOutputData() {
        return glomeruli2DOutputData;
    }

    public TIFFPlanesImageCache<UnsignedIntType> getGlomeruli3DOutputData() {
        return glomeruli3DOutputData;
    }
}
