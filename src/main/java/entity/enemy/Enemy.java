package entity.enemy;

import entity.HitBox;
import entity.Movable;
import geometry.TriangleMesh;
import render.transformation.Transformation;
import util.Color;

import java.util.List;

import static geometry.Vertex.vertex;
import static render.Renderer.renderFrom;
import static render.transformation.Transformation.rotate;
import static render.transformation.Transformation.scale;
import static render.transformation.Transformation.translate;

public class Enemy extends Movable {
    private final TriangleMesh model;
    private final Color color;
    private final HitBox hitBox;
    private final EnemyTrajectory enemyTrajectory;

    public static final String ENEMY_MODEL_FILEPATH = "duck.tri";
    public static final int TOTAL_ENEMIES = 3;

    public Enemy(TriangleMesh model, Color color, int trajectoryId) {
        super(vertex(0, 0, 0));
        this.model = model;
        this.color = color;
        this.hitBox = new HitBox(0, 0, 0);
        this.enemyTrajectory = new EnemyTrajectory(trajectoryId);
    }

    @Override
    public HitBox getHitBox() {
        return hitBox;
    }

    @Override
    public void update() {
        setPosition(enemyTrajectory.nextPosition());
    }

    @Override
    public void render() {
        List<Transformation> transformations = List.of(
                scale(getPosition(), .15f, .1f, .1f),
                translate(vertex(getPosition().getX(), getPosition().getY() + 50, getPosition().getZ())),
                rotate(vertex(1, 0, 0), -90)
        );

        renderFrom(model, color, transformations);
    }
}
