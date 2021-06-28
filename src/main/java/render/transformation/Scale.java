package render.transformation;

import geometry.Vertex;

import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;

public class Scale implements Transformation {
    public final Vertex position;
    public final float x;
    public final float y;
    public final float z;

    public Scale(Vertex position, float x, float y, float z) {
        this.position = position;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void apply() {
        glTranslatef(position.getX(), position.getY(), position.getZ());
        glScalef(x, y, z);
        glTranslatef(-position.getX(), -position.getY(), -position.getZ());
    }
}
