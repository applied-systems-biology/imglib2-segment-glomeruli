package org.hkijena.segment_glomeruli;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.integer.UnsignedIntType;
import org.hkijena.segment_glomeruli.caches.OMETIFFImageCache;
import org.hkijena.segment_glomeruli.caches.TIFFPlanesImageCache;
import org.hkijena.segment_glomeruli.data.GlomeruliQuantificationResult;
import org.hkijena.segment_glomeruli.data.TissueQuantificationResult;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class DataInterface {

    private Path outputDirectory;

    private double voxelSizeXY;
    private double voxelSizeZ;

    private OMETIFFImageCache<UnsignedByteType> inputData;
    private TIFFPlanesImageCache<UnsignedByteType> tissueOutputData;
    private TIFFPlanesImageCache<UnsignedByteType> glomeruli2DOutputData;
    private TIFFPlanesImageCache<UnsignedIntType> glomeruli3DOutputData;

    private TissueQuantificationResult tissueQuantificationResult = new TissueQuantificationResult();
    private GlomeruliQuantificationResult glomeruliQuantificationResult = new GlomeruliQuantificationResult();

    private volatile long tissuePixelCount = 0;

    public DataInterface(Path inputImageFile, Path outputDirectory, double voxelSizeXY, double voxelSizeZ) {
        this.outputDirectory = outputDirectory;
        this.voxelSizeXY = voxelSizeXY;
        this.voxelSizeZ = voxelSizeZ;
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

    public double getVoxelSizeXY() {
        return voxelSizeXY;
    }

    public double getVoxelSizeZ() {
        return voxelSizeZ;
    }

    public long getTissuePixelCount() {
        return tissuePixelCount;
    }

    public synchronized void addTissuePixelCount(long count) {
        this.tissuePixelCount += count;
    }

    private void saveTissueQuantificationResults() {
        try(FileWriter writer = new FileWriter(outputDirectory.resolve("tissue_quantified.json").toFile())) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(tissueQuantificationResult);
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveGlomeruliQuantificationResults() {
        try(FileWriter writer = new FileWriter(outputDirectory.resolve("glomeruli.json").toFile())) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(glomeruliQuantificationResult);
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveQuantificationResults() {
        saveTissueQuantificationResults();
        saveGlomeruliQuantificationResults();
    }

    public TissueQuantificationResult getTissueQuantificationResult() {
        return tissueQuantificationResult;
    }

    public GlomeruliQuantificationResult getGlomeruliQuantificationResult() {
        return glomeruliQuantificationResult;
    }
}
