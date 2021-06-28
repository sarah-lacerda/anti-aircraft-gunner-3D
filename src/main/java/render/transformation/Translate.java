package render.transformation;

import geometry.Vertex;

import static org.lwjgl.opengl.GL11.glTranslatef;

public class Translate implements Transformation {

    public final Vertex position;

    public Translate(Vertex position) {
        this.position = position;
    }

    @Override
    public void apply() {
        glTranslatef(position.getX(), position.getY(), position.getZ());

    }
}
