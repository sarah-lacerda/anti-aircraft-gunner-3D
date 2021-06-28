package glfw;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_C;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

public enum Commands {
    MOVE_PLAYER_FORWARD(GLFW_KEY_SPACE),
    MOVE_PLAYER_LEFT(GLFW_KEY_A),
    MOVE_PLAYER_RIGHT(GLFW_KEY_D),
    SWITCH_CAMERA_VIEW(GLFW_KEY_C);

    final int glfwKey;

    Commands(int glfwKey) {
        this.glfwKey = glfwKey;
    }
}
