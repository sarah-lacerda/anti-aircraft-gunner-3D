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
}
