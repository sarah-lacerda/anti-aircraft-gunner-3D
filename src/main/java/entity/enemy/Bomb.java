package entity.enemy;

import entity.HitBox;
import entity.Movable;
import geometry.TriangleMesh;
import geometry.Vertex;
import render.transformation.Transformation;
import util.Color;

import java.util.List;

import static geometry.Vertex.vertex;
import static render.Renderer.renderFrom;
import static render.transformation.Transformation.scale;
import static render.transformation.Transformation.translate;
import static util.Color.BLACK;

public class Bomb extends Movable {
    private final HitBox hitBox;

    private static final String BOMB_MODEL_FILEPATH = "model/bomb.tri";
    private static final TriangleMesh MODEL = TriangleMesh.loadFromTRI(BOMB_MODEL_FILEPATH);
    private static final Color COLOR = BLACK;

    public Bomb(Vertex position) {
        super(position);
        this.hitBox = new HitBox(5, 5, 5);
        ;
    }

    @Override
    public HitBox getHitBox() {
        return hitBox;
    }

    @Override
    public void render() {
        List<Transformation> transformations = List.of(
                scale(getPosition(), 10, 10, 10),
                translate(getPosition())
        );
        renderFrom(MODEL, COLOR, transformations);
    }

    @Override
    public void update() {
        setPosition(vertex(getPosition().getX(), getPosition().getY() - .2f, getPosition().getZ()));
    }
}
