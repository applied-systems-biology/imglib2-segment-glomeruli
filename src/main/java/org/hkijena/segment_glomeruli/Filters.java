package org.hkijena.segment_glomeruli;

import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.img.Img;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.integer.UnsignedByteType;

public class Filters {

    public static <T extends Type<T>> void setTo(Img<T> target, T value) {
        Cursor<T> cursor = target.cursor();
        RandomAccess<T> access = target.randomAccess();
        while(cursor.hasNext()) {
            cursor.fwd();
            access.setPosition(cursor);
            access.get().set(value);
        }
    }

    public static <T extends Type<T>> void median(Img<T> src, Img<T> target, int sz, T zero) {
        if(zero != null)
            setTo(target, zero);
        int border = sz / 2;
        final RectangleShape shape = new RectangleShape( sz / 2, true );
    }
}
