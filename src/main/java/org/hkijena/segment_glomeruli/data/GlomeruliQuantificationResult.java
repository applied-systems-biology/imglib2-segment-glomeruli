package org.hkijena.segment_glomeruli.data;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class GlomeruliQuantificationResult {
    public Map<Integer, Glomerulus> data = new HashMap<>();

    @SerializedName("valid-glomeruli-number")
    public int validGlomeruliNumber = 0;

    @SerializedName("invalid-glomeruli-number")
    public int invalidGlomeruliNumber = 0;

    @SerializedName("valid-glomeruli-diameter-average")
    public double validGlomeruliDiameterAverage = 0;

    @SerializedName("valid-glomeruli-diameter-variance")
    public double validGlomeruliDiameterVariance = 0;
}
