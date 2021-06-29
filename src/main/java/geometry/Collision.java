package geometry;

import entity.Entity;
import entity.Gas;
import entity.Player;
import scene.SceneManager;

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

    public static void handleCollisions(SceneManager sceneManager) {
        for (int i = 0; i < sceneManager.getMovableEntities().size(); i++) {
            for (int j = 0; j < sceneManager.getEntities().size(); j++) {
                final Entity entity1 = sceneManager.getEntities().get(i);
                final Entity entity2 = sceneManager.getEntities().get(j);
                if (collisionBetween(entity1, entity2)) {
                    if (entity1.getClass() == Player.class) {
                        if (entity2.getClass() == Gas.class) {
                            sceneManager.removeEntity(entity2);
                        }
                    }
                }
            }
        }
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
