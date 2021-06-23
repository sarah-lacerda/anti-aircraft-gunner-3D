package geometry;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Vector {

    private final float x;
    private final float y;
    private final float z;
    private final float magnitude;

    public Vector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.magnitude = (float) sqrt(pow(x, 2) + pow(y, 2) + pow(z, 2));
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

    public float getMagnitude() {
        return magnitude;
    }

    public static Vector vector(Vertex p, Vertex q) {
        return new Vector(q.getX() - p.getX(), q.getY() - p.getY(), q.getZ() - p.getZ());
    }

    public Vector versor() {
        return new Vector(x / magnitude, y / magnitude, z / magnitude);
    }
}
