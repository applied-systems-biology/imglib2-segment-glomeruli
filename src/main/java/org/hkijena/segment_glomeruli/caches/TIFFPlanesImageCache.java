package org.hkijena.segment_glomeruli.caches;

import io.scif.img.ImgOpener;
import io.scif.img.ImgSaver;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import org.hkijena.segment_glomeruli.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TIFFPlanesImageCache<T extends RealType<T> & NativeType<T>> {

    private Path directory;
    private ImgSaver saver = new ImgSaver();
    private ImgOpener opener = new ImgOpener();
    private T imgDataType;
    private ImgFactory<T> factory;
    private long width;
    private long height;

    public TIFFPlanesImageCache(Path directory, T imgDataType, long width, long height) {
        this.directory = directory;
        this.imgDataType = imgDataType;
        this.factory = new ArrayImgFactory<>(imgDataType);
        this.width = width;
        this.height = height;

        try {
            Utils.ensureDirectory(directory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Path getPathForPlane(long z) {
        return directory.resolve("z" + String.format("%04d", z) + ".tif");
    }

    public Img<T> getOrCreatePlane(long z) {
        if(Files.exists(getPathForPlane(z))) {
            return opener.openImgs(getPathForPlane(z).toString(), imgDataType).get(0);
        }
        else {
            return factory.create(getWidth(), getHeight());
        }
    }

    public void setPlane(long z, Img<T> img) {
        saver.saveImg(getPathForPlane(z).toString(), img);
    }

    public long getWidth() {
        return width;
    }

    public long getHeight() {
        return height;
    }
}
