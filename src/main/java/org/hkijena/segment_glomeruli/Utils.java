package org.hkijena.segment_glomeruli;

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
