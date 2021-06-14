package scene;

import geometry.Vertex;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import static java.lang.Math.toRadians;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glLoadMatrixf;
import static org.lwjgl.opengl.GL11.glMatrixMode;

public class Camera {

    private Vertex position;
    private Vertex target;

    public Camera(Vertex position, Vertex target) {
        this.position = position;
        this.target = target;
    }

    public Camera() {
        this.position = new Vertex(0, 0, 10);
        this.target = new Vertex(0, 0, 0);
    }

    public void setPosition(Vertex position) {
        this.position = position;
    }

    public void setTarget(Vertex target) {
        this.target = target;
    }

    public Vertex getPosition() {
        return position;
    }

    public Vertex getTarget() {
        return target;
    }

    public void update() {
        final Matrix4f matrix4f = new Matrix4f();
        matrix4f.setPerspective((float) toRadians(45.0f), 1.0f, 0.01f, 100f);
        glMatrixMode(GL_PROJECTION);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            glLoadMatrixf(matrix4f.get(stack.mallocFloat(16)));
        }
        matrix4f.setLookAt(position.getX(), position.getY(), position.getZ(),
                target.getX(), target.getY(), target.getZ(),
                0.0f, 1.0f, 0.0f);
        glMatrixMode(GL_MODELVIEW);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            glLoadMatrixf(matrix4f.get(stack.mallocFloat(16)));
        }
    }
}
