package geometry;

import entity.Building;
import entity.Entity;
import entity.Gas;
import entity.Movable;
import entity.Player;
import entity.Road;
import entity.enemy.Bomb;
import scene.SceneManager;

import java.util.List;

public class Collision {

    private Collision() {
    }

    public static boolean isPlayerOver(Class entityType, SceneManager sceneManager) {
        return sceneManager
                .getEntities()
                .stream()
                .filter(entity -> entity.getClass() == entityType)
                .anyMatch(entity -> collisionBetween(entity, sceneManager.getPlayer()));
    }

    public static boolean isPlayerOverDestroyedRoad(SceneManager sceneManager) {
        return sceneManager
                .getEntities()
                .stream()
                .filter(entity -> entity.getClass() == Road.class)
                .filter(entity -> ((Road) entity).isDamaged())
                .anyMatch(entity -> collisionBetween(entity, sceneManager.getPlayer()));
    }

    public static void handleCollisions(SceneManager sceneManager) {
        List<Movable> movables = sceneManager.getMovableEntities();
        for (int i = 0; i < movables.size(); i++) {
            for (int j = 0; j < sceneManager.getEntities().size(); j++) {
                final Entity entity1 = movables.get(i);
                final Entity entity2 = sceneManager.getEntities().get(j);
                if (collisionBetween(entity1, entity2)) {
                    if (collisionBetweenPlayerAndGasContainer(entity1, entity2)) {
                        sceneManager.removeEntity(entity2);
                        ((Player) entity1).increaseFuel(20);
                    } else if (entity1.getClass() == Bomb.class) {
                        if (entity2.getClass() == Road.class) {
                            ((Road) entity2).damage();
                        } else if (entity2.getClass() == Building.class) {
                            sceneManager.removeEntity(entity2);
                        }
                    }
                }
            }
        }
    }

    private static boolean collisionBetweenPlayerAndGasContainer(Entity potentialPlayer, Entity potentialGasContainer) {
        return potentialPlayer.getClass() == Player.class && potentialGasContainer.getClass() == Gas.class;
    }

    private static boolean collisionBetween(Entity entity1, Entity entity2) {
        if (distanceBetween(entity1.getPosition().getX(), entity2.getPosition().getX()) >
                ((entity1.getHitBox().width + entity2.getHitBox().width) / 2f)) {
            return false;
        }
        if (distanceBetween(entity1.getPosition().getY(), entity2.getPosition().getY()) >
                ((entity1.getHitBox().height + entity2.getHitBox().height) / 2f)) {
            return false;
        }
        if (distanceBetween(entity1.getPosition().getZ(), entity2.getPosition().getZ()) >
                ((entity1.getHitBox().depth + entity2.getHitBox().depth) / 2f)) {
            return false;
        }

        return true;
    }

    private static float distanceBetween(float c1, float c2) {
        return Math.abs(c1 - c2);
    }
}
