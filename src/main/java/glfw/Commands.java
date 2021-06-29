package glfw;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_C;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

public enum Commands {
    MOVE_PLAYER_FORWARD(GLFW_KEY_SPACE),
    MOVE_PLAYER_LEFT(GLFW_KEY_A),
    MOVE_PLAYER_RIGHT(GLFW_KEY_D),
    SWITCH_CAMERA_VIEW(GLFW_KEY_C),
    MOVE_CAMERA_UP(GLFW_KEY_W),
    MOVE_CAMERA_DOWN(GLFW_KEY_S);

    final int glfwKey;

    Commands(int glfwKey) {
        this.glfwKey = glfwKey;
    }
}
