package com.agentmesh.agentmesh.memory.hnsw;

/**
 * Static helper methods for comparing embedding vectors.
 * HNSW ka poora graph isi "distance" ke upar chalta hai.
 */
public final class VectorMath {

    private VectorMath() {
    }   // utility class, instantiate nahi hogi

    /**
     * Dot product: sum of a[i] * b[i].
     */
    public static double dot(float[] a, float[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("Vector length mismatch: " + a.length + " vs " + b.length);
        }
        double sum = 0;
        for(int i = 0; i < a.length; i++){
            sum += (double) a[i] * b[i];
        }
       return sum;
    }

    /**
     * Magnitude (length) of a vector: sqrt(sum of squares).
     */
    public static double magnitude(float[] v) {
        return Math.sqrt(dot(v,v));
    }

    /**
     * Returns value in [-1, 1]. 1 = same direction, 0 = unrelated.
     */
    public static double cosineSimilarity(float[] a, float[] b) {
        if (a.length == 0 || b.length == 0) {
            throw new IllegalArgumentException("Empty vector");
        }
        if (a.length != b.length) {
            throw new IllegalArgumentException(
                    "Vector length mismatch: " + a.length + " vs " + b.length);
        }
        double magA = magnitude(a);
        double magB = magnitude(b);
        if (magA == 0 || magB == 0) {
            return 0.0;
        }
        return  dot(a, b) / (magA * magB);
    }

    /**
     * HNSW ko "distance" chahiye (chhota = paas), isliye similarity ko flip karte hain.
     */
    public static double cosineDistance(float[] a, float[] b) {
        return 1 - cosineSimilarity(a, b);
    }
}