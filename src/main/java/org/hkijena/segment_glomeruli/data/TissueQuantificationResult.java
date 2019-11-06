package org.hkijena.segment_glomeruli.data;

import com.google.gson.annotations.SerializedName;

public class TissueQuantificationResult {

    @SerializedName("num-pixels")
    public long numPixels = 0;

    @SerializedName("volume-microns3")
    public double volumeMicrons3 = 0;

}
