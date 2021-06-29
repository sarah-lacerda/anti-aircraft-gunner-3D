package entity;

import geometry.TriangleMesh;
import geometry.Vertex;
import render.transformation.Transformation;
import util.Color;

import java.util.List;

import static geometry.Vertex.vertex;
import static render.Renderer.renderDebug;
import static render.Renderer.renderFrom;
import static render.transformation.Transformation.rotate;
import static render.transformation.Transformation.scale;
import static render.transformation.Transformation.translate;
import static util.Debug.DEBUG_ENABLED;

public class Gas extends Entity {

    private final TriangleMesh model;
    private final Color color;
    private final HitBox hitBox;
    private float rotationAngle;

    public static final int TOTAL_AMOUNT_OF_GAS_CONTAINERS = 10;
    public static final String GAS_MODEL_FILEPATH = "model/gasContainer.tri";

    public Gas(Vertex position, TriangleMesh model, Color color) {
        super(position);
        this.model = model;
        this.color = color;
        this.hitBox = new HitBox(1, 1, 1);
    }

    @Override
    public HitBox getHitBox() {
        return hitBox;
    }

    @Override
    public void render() {
        List<Transformation> transformations = List.of(
                scale(getPosition(), .02f, .02f, .02f),
                translate(vertex(getPosition().getX(), getPosition().getY(), getPosition().getZ())),
                rotate(vertex(1, 0, 0), 250),
                rotate(vertex(0, 0, 1), rotationAngle--)
        );

        renderFrom(model, color, transformations);

        if (DEBUG_ENABLED) {
            List<Transformation> hitBoxTransformations = List.of(
                    translate(vertex(getPosition().getX(), getPosition().getY(), getPosition().getZ())),
                    rotate(vertex(1, 0, 0), 250),
                    rotate(vertex(0, 0, 1), rotationAngle--)
            );
            renderDebug(hitBox, hitBoxTransformations);
        }
    }
}
