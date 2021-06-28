package render.transformation;

import geometry.Vertex;

import static org.lwjgl.opengl.GL11.glRotatef;

public class Rotate implements Transformation {

    public final Vertex around;
    public final float angle;

    public Rotate(Vertex around, float angle) {
        this.around = around;
        this.angle = angle;
    }

    @Override
    public void apply() {
        glRotatef(angle, around.getX(), around.getY(), around.getZ());
    }
}
