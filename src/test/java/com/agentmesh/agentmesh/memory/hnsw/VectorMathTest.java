package com.agentmesh.agentmesh.memory.hnsw;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VectorMathTest {

    @Test
    void dot_basicExample() {
        assertEquals(32.0, VectorMath.dot(new float[]{1, 2, 3}, new float[]{4, 5, 6}), 1e-6);
    }
    @Test
    void magnitude_threeFourFive() {
        assertEquals(5.0, VectorMath.magnitude(new float[]{3, 4}), 1e-6);
    }

    @Test
    void cosineSimilarity_lengthMismatch() {
        assertThrows(IllegalArgumentException.class,
                () -> VectorMath.cosineSimilarity(new float[]{1, 2}, new float[]{1, 2, 3}));
    }
    @Test
    void cosineSimilarity_sameVector() {
        float[] v = {1, 2, 3};
        assertEquals(1.0, VectorMath.cosineSimilarity(v, v), 1e-6);
    }

    @Test
    void cosineSimilarity_scaledVector() {
        float[] a = {1, 2, 3};
        float[] b = {2, 4, 6};   // a ka double, yaani same direction, bas lamba
        assertEquals(1.0, VectorMath.cosineSimilarity(a, b), 1e-6);
    }

    @Test
    void magnitude_threeElements() {
        assertEquals(Math.sqrt(14), VectorMath.magnitude(new float[]{1, 2, 3}), 1e-6);
    }
    @Test
    void cosineSimilarity_perpendicular() {
        float[] a = {1, 0};
        float[] b = {0, 1};
        assertEquals(0.0, VectorMath.cosineSimilarity(a, b), 1e-6);
    }

    @Test
    void cosineSimilarity_opposite() {
        float[] a = {1, 0};
        float[] b = {-1, 0};
        assertEquals(-1.0, VectorMath.cosineSimilarity(a, b), 1e-6);
    }

    @Test
    void cosineSimilarity_zeroVector() {
        float[] zero = {0, 0};
        float[] b = {3, 4};
        // zero vector ki direction nahi hoti, isliye hamara decision: 0.0 ("unrelated")
        assertEquals(0.0, VectorMath.cosineSimilarity(zero, b), 1e-6);
    }

    @Test
    void cosineSimilarity_emptyVector() {
        assertThrows(IllegalArgumentException.class,
                () -> VectorMath.cosineSimilarity(new float[]{}, new float[]{}));
    }

    @Test
    void cosineDistance_similarVectors() {
        float[] a = {3, 4};
        float[] b = {4, 3};
        // similarity 0.96, to distance = 1 - 0.96 = 0.04
        assertEquals(0.04, VectorMath.cosineDistance(a, b), 1e-6);
    }

    @Test
    void cosineDistance_similarIsSmallerThanUnrelated() {
        double similar = VectorMath.cosineDistance(new float[]{3, 4}, new float[]{4, 3});
        double unrelated = VectorMath.cosineDistance(new float[]{1, 0}, new float[]{0, 1});
        // HNSW isi property pe chalta hai: paas wale ka distance chhota hona chahiye
        assertTrue(similar < unrelated);
    }
}