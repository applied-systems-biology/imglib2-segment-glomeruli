package org.hkijena.segment_glomeruli.caches;

import io.scif.img.ImgOpener;
import io.scif.img.SCIFIOImgPlus;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;
import org.hkijena.segment_glomeruli.Filters;
import org.hkijena.segment_glomeruli.Main;

import java.nio.file.Path;

public class OMETIFFImageCache<T extends RealType<T> & NativeType<T>> {

    private ImgFactory<T> factory;
    private Path fileName;
    private SCIFIOImgPlus<T> img;
    private T imageDataType;

    public OMETIFFImageCache(Path fileName, T imageDataType) {
        this.fileName = fileName;
        this.factory = new ArrayImgFactory<>(imageDataType);
        img = Main.IMGOPENER.openImgs(fileName.toString(),imageDataType).get(0);
        this.imageDataType = imageDataType;
    }

    public SCIFIOImgPlus<T> getImg() {
        return img;
    }

    public long getXSize() {
        return img.dimension(0);
    }

    public long getYSize() {
        return img.dimension(1);
    }

    public long getZSize() {
        return img.dimension(2);
    }

    public Img<T> getPlane(long z) {
        Img<T> result = factory.create(img.dimension(0), img.dimension(1));
        Filters.copy(Views.hyperSlice(img, 2, z), result);
        return result;
    }

}
