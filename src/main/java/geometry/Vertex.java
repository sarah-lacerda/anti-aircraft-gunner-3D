package geometry;

public class Vertex {
    private final float x;
    private final float y;
    private final float z;

    public Vertex(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public static Vertex vertex(float x, float y, float z) {
        return new Vertex(x, y, z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final float threshold = 1.0001f;
        Vertex vertex = (Vertex) o;

        if (Math.abs(vertex.x - x) >= threshold) return false;
        if (Math.abs(vertex.y - y) >= threshold) return false;
        return !(Math.abs(vertex.z - z) >= threshold);
    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        result = 31 * result + (z != +0.0f ? Float.floatToIntBits(z) : 0);
        return result;
    }
}
