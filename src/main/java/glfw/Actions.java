package glfw;

import entity.Road;
import entity.Terrain;
import glfw.listener.KeyListener;
import scene.SceneManager;

import static geometry.Collision.handleCollisions;
import static geometry.Collision.isPlayerOver;
import static glfw.Commands.MOVE_PLAYER_FORWARD;
import static glfw.Commands.MOVE_PLAYER_LEFT;
import static glfw.Commands.MOVE_PLAYER_RIGHT;
import static glfw.Commands.SWITCH_CAMERA_VIEW;

public class Actions {

    private Actions() {
    }

    public static void handleActions(SceneManager sceneManager) {
        if (KeyListener.getInstance().isKeyPressed(MOVE_PLAYER_FORWARD.glfwKey)) {
            if (!isPlayerOver(Terrain.class, sceneManager)) {
                sceneManager.movePlayerForward();
            }
        }
        if (KeyListener.getInstance().isKeyPressed(MOVE_PLAYER_LEFT.glfwKey)) {
            sceneManager.movePlayerLeft();
        }
        if (KeyListener.getInstance().isKeyPressed(MOVE_PLAYER_RIGHT.glfwKey)) {
            sceneManager.movePlayerRight();
        }
        if (KeyListener.getInstance().isKeyPressed(SWITCH_CAMERA_VIEW.glfwKey)) {
            sceneManager.toggleCameraView();
        }

        haltPlayerIfGoingOverTerrain(sceneManager);
        handleCollisions(sceneManager);
    }

    private static void haltPlayerIfGoingOverTerrain(SceneManager sceneManager) {
        if (isPlayerOver(Terrain.class, sceneManager) && isPlayerOver(Road.class, sceneManager)) {
            sceneManager.getPlayer().setMovingForward(false);
            sceneManager.getPlayer().moveBackwards();
        }
    }

}
