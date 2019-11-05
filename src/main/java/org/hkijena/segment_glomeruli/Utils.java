package org.hkijena.segment_glomeruli;

import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessible;
import net.imglib2.type.Type;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Utils {

    public static void ensureDirectory(Path path) throws IOException {
        if(!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }
}
