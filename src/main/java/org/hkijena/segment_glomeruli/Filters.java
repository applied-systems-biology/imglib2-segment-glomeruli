package org.hkijena.segment_glomeruli;

import net.imglib2.*;
import net.imglib2.RandomAccess;
import net.imglib2.algorithm.neighborhood.Neighborhood;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.algorithm.stats.Max;
import net.imglib2.img.Img;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.IntegerType;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.AbstractRealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.Views;

import java.util.*;

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

    public static <T extends RealType<T>> void median(Img<T> src, Img<T> target, int sz, T zero) {
        if(zero != null)
            setTo(target, zero);
        int border = sz / 2;
        long[] border_arr = new long[src.numDimensions()];
        Arrays.fill(border_arr, -border);

        RandomAccessibleInterval<T> src_ = Views.interval(src, Views.expandZero(src, border_arr));
        RandomAccessibleInterval<T> target_ = Views.interval(target, Views.expandZero(target, border_arr));

        final RectangleShape shape = new RectangleShape( border, true );
        RectangleShape.NeighborhoodsIterableInterval<T> neighborhoods = shape.neighborhoods(src_);

        Cursor<Neighborhood<T>> shape_cursor = neighborhoods.cursor();
        Cursor<T> src_center_cursor = Views.iterable(src_).cursor();
        Cursor<T> target_center_cursor = Views.iterable(target_).cursor();

        List<RealType<T>> buffer = new ArrayList<>();

        while(shape_cursor.hasNext()) {
            shape_cursor.fwd();
            src_center_cursor.fwd();
            target_center_cursor.fwd();

            Cursor<T> ncursor = shape_cursor.get().cursor();

            buffer.clear();
            buffer.add(src_center_cursor.get());
            while(ncursor.hasNext()) {
                ncursor.fwd();
                buffer.add(ncursor.get().copy());
            }
            buffer.sort((tRealType, t1) -> tRealType.compareTo((T)t1));

            target_center_cursor.get().set((T)buffer.get(buffer.size() / 2));
        }
    }

    public static <T extends RealType<T>> T getMax(Img<T> src) {
        Cursor<T> cursor = src.cursor();
        T max = null;
        while(cursor.hasNext()) {
            cursor.fwd();
            if(max == null || cursor.get().compareTo(max) > 0)
                max = cursor.get().copy();
        }
        return max;
    }

    public static <T extends RealType<T>> void normalizeByMax(Img<T> target) {
        T max_value = getMax(target);
        Cursor<T> cursor = target.cursor();
        while(cursor.hasNext()) {
            cursor.fwd();
            T v = cursor.get().copy();
            v.div(max_value);
            cursor.get().set(v);
        }
    }
}
