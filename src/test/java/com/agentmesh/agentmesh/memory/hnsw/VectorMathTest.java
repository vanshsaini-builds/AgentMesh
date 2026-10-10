import com.agentmesh.agentmesh.memory.hnsw.VectorMath;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VectorMathTest git git{

    @Test
    void dot_basicExample() {
        assertEquals(32.0, VectorMath.dot(new float[]{1, 2, 3}, new float[]{4, 5, 6}), 1e-6);
    }
}