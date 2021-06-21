package glfw;

import geometry.Vertex;
import glfw.listener.KeyListener;
import scene.Camera;
import scene.SceneManager;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_ADD;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_SUBTRACT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

public class Actions {

    private Actions() {
    }

    public static void handleActions(SceneManager sceneManager) {
        final Camera camera = sceneManager.getCamera();
        if (KeyListener.getInstance().isKeyPressed(GLFW_KEY_UP)) {
            Vertex currPos = camera.getPosition();
            camera.setPosition(new Vertex(currPos.getX(), currPos.getY() + 1, currPos.getZ()));
        }
        if (KeyListener.getInstance().isKeyPressed(GLFW_KEY_DOWN)) {
            Vertex currPos = camera.getPosition();
            camera.setPosition(new Vertex(currPos.getX(), currPos.getY() - 1, currPos.getZ()));
        }
        if (KeyListener.getInstance().isKeyPressed(GLFW_KEY_RIGHT)) {
            Vertex currPos = camera.getPosition();
            camera.setPosition(new Vertex(currPos.getX() + 1, currPos.getY(), currPos.getZ()));
        }
        if (KeyListener.getInstance().isKeyPressed(GLFW_KEY_LEFT)) {
            Vertex currPos = camera.getPosition();
            camera.setPosition(new Vertex(currPos.getX() - 1, currPos.getY(), currPos.getZ()));
        }
        if (KeyListener.getInstance().isKeyPressed(GLFW_KEY_KP_SUBTRACT)) {
            Vertex currPos = camera.getPosition();
            camera.setPosition(new Vertex(currPos.getX(), currPos.getY(), currPos.getZ() + 1));
        }
        if (KeyListener.getInstance().isKeyPressed(GLFW_KEY_KP_ADD)) {
            Vertex currPos = camera.getPosition();
            camera.setPosition(new Vertex(currPos.getX(), currPos.getY(), currPos.getZ() - 1));
        }
        camera.update();
    }

}
