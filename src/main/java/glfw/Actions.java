package glfw;

import entity.Road;
import entity.Terrain;
import entity.enemy.Enemy;
import glfw.listener.KeyListener;
import scene.SceneManager;

import java.util.List;

import static entity.enemy.Enemy.PROBABILITY_OF_SHOOTING;
import static geometry.Collision.handleCollisions;
import static geometry.Collision.isPlayerOver;
import static geometry.Collision.isPlayerOverDestroyedRoad;
import static glfw.Commands.MOVE_CAMERA_DOWN;
import static glfw.Commands.MOVE_CAMERA_UP;
import static glfw.Commands.MOVE_PLAYER_FORWARD;
import static glfw.Commands.MOVE_PLAYER_LEFT;
import static glfw.Commands.MOVE_PLAYER_RIGHT;
import static glfw.Commands.SWITCH_CAMERA_VIEW;
import static util.Randomizer.randomBoolean;
import static util.Randomizer.randomIntWithinRange;

public class Actions {

    private Actions() {
    }

    public static void handleActions(SceneManager sceneManager) {
        haltPlayerIfGoingOverTerrain(sceneManager);
        handleUserInput(sceneManager);
        handleCollisions(sceneManager);
        handleEnemyShooting(sceneManager);
        sceneManager.destroyEntitiesOutOfReach();
    }

    private static void handleEnemyShooting(SceneManager sceneManager) {
        if (randomBoolean(PROBABILITY_OF_SHOOTING)) {
            List<Enemy> enemies = sceneManager.getEnemies();
            Enemy randomEnemy = enemies.get(randomIntWithinRange(0, enemies.size() - 1));
            sceneManager.addEntity(randomEnemy.shoot());
        }
    }

    private static void handleUserInput(SceneManager sceneManager) {
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
        if (KeyListener.getInstance().isKeyPressed(MOVE_CAMERA_UP.glfwKey)) {
            if (sceneManager.getCamera().isPerspectiveViewEnabled()) {
                sceneManager.getPlayer().cameraLookUp();
            }
        }
        if (KeyListener.getInstance().isKeyPressed(MOVE_CAMERA_DOWN.glfwKey)) {
            if (sceneManager.getCamera().isPerspectiveViewEnabled()) {
                sceneManager.getPlayer().cameraLookDown();
            }
        }
    }

    private static void haltPlayerIfGoingOverTerrain(SceneManager sceneManager) {
        boolean isPlayerHeadingTowardsTerrain = isPlayerOver(Terrain.class, sceneManager) &&
                isPlayerOver(Road.class, sceneManager);

        if (isPlayerHeadingTowardsTerrain || isPlayerOverDestroyedRoad(sceneManager)) {
            sceneManager.getPlayer().setMovingForward(false);
            sceneManager.getPlayer().moveBackwards();
        }
    }

}
