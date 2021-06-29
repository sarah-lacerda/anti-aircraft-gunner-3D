package scene;

import geometry.Vertex;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import static geometry.Vertex.vertex;
import static java.lang.Math.toRadians;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glLoadMatrixf;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static scene.World.WORLD_DEPTH;
import static scene.World.WORLD_HEIGHT;
import static scene.World.WORLD_WIDTH;
import static scene.World.X_LOWER_BOUND;
import static scene.World.X_UPPER_BOUND;
import static scene.World.Y_LOWER_BOUND;
import static scene.World.Y_UPPER_BOUND;
import static scene.World.Z_LOWER_BOUND;

public class Camera {

    private Vertex position;
    private Vertex target;
    private boolean perspectiveView;

    private static final Vertex DEFAULT_ORTHOGRAPHIC_POSITION = new Vertex(-100, 100, 100);
    private static final Vertex DEFAULT_ORTHOGRAPHIC_TARGET = new Vertex(-1, 0, -1);

    public Camera(Vertex position, Vertex target) {
        this.position = position;
        this.target = target;
    }

    public Camera() {
        this.position = new Vertex(0, 5f, 0);
        this.target = new Vertex(0, 0, 0);
        perspectiveView = false;
    }

    public boolean toggleCameraPerspective() {
        perspectiveView = !perspectiveView;
        return perspectiveView;
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

    public boolean isPerspectiveViewEnabled() {
        return perspectiveView;
    }

    public void update() {
        final Matrix4f matrix4f = new Matrix4f();
        if (perspectiveView) {
            matrix4f.setPerspective(
                    (float) toRadians(45.0f),
                    1.0f,
                    0.01f,
                    WORLD_WIDTH + WORLD_HEIGHT + WORLD_DEPTH
            );
        } else {
            matrix4f.ortho(
                    X_LOWER_BOUND - 22,
                    X_UPPER_BOUND + 22,
                    Y_LOWER_BOUND + 5,
                    Y_UPPER_BOUND,
                    Z_LOWER_BOUND,
                    WORLD_WIDTH + WORLD_HEIGHT + WORLD_DEPTH
            );
            setTarget(DEFAULT_ORTHOGRAPHIC_TARGET);
            transitionPositionToDefaultOrthographic();
        }
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

    private void transitionPositionToDefaultOrthographic() {
        if (!getPosition().equals(DEFAULT_ORTHOGRAPHIC_POSITION)) {
            if (getPosition().getX() > DEFAULT_ORTHOGRAPHIC_POSITION.getX()) {
                setPosition(vertex(getPosition().getX() - 1.0f, getPosition().getY(), getPosition().getZ()));
            } else {
                setPosition(vertex(getPosition().getX() + 1f, getPosition().getY(), getPosition().getZ()));
            }

            if (getPosition().getY() > DEFAULT_ORTHOGRAPHIC_POSITION.getY()) {
                setPosition(vertex(getPosition().getX(), getPosition().getY() - 1.0f, getPosition().getZ()));
            } else {
                setPosition(vertex(getPosition().getX(), getPosition().getY() + 1f, getPosition().getZ()));
            }

            if (getPosition().getZ() > DEFAULT_ORTHOGRAPHIC_POSITION.getZ()) {
                setPosition(vertex(getPosition().getX(), getPosition().getY(), getPosition().getZ() - 1.0f));
            } else {
                setPosition(vertex(getPosition().getX(), getPosition().getY(), getPosition().getZ() + 1.0f));
            }
        }
    }
}
