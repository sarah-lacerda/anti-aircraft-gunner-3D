package entity;

import geometry.TriangleMesh;
import geometry.Vertex;
import render.transformation.Transformation;
import util.Color;

import java.util.List;

import static geometry.Vertex.vertex;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;
import static java.util.List.of;
import static render.Renderer.FRAMES_PER_SECOND;
import static render.Renderer.renderFrom;
import static render.transformation.Transformation.rotate;
import static render.transformation.Transformation.scale;
import static render.transformation.Transformation.translate;
import static scene.World.WORLD_WIDTH;

public class Player extends Movable {

    private final TriangleMesh model;
    private final Color color;
    private boolean movingForward;
    private float movementAngle = 0;

    public final static float SPEED = 1f / 20f;
    private static final float UNIT_OF_MOVEMENT_PER_FRAME = (WORLD_WIDTH / (float) FRAMES_PER_SECOND) * SPEED;
    public static final String PLAYER_MODEL_FILEPATH = "car2.tri";

    public Player(Vertex position, TriangleMesh model, Color color) {
        super(position);
        this.model = model;
        this.color = color;
        this.movingForward = false;
    }

    public void moveLeft() {
        movementAngle += 0.5f;
    }

    public void moveRight() {
        movementAngle -= 0.5f;
    }

    public void beginMoveForward() {
        if (!movingForward) {
            moveForward();
        }
    }

    private void moveForward() {
        movingForward = true;
        setPosition(vertex(
                getPosition().getX() +
                        (float) cos(toRadians(movementAngle - 270)) *
                                UNIT_OF_MOVEMENT_PER_FRAME
                ,
                getPosition().getY()
                ,
                getPosition().getZ() -
                        (float) sin(toRadians(movementAngle - 270)) *
                                UNIT_OF_MOVEMENT_PER_FRAME
        ));

    }

    public Vertex getFirstPersonCameraPosition() {
        return vertex(getPosition().getX(), getPosition().getY() + 2.0f, getPosition().getZ());
    }

    public Vertex getFirstPersonCameraTarget() {
        return vertex(
                getPosition().getX() + (float) cos(toRadians(movementAngle - 270)) * 5,
                getPosition().getY() + 2f,
                getPosition().getZ() - (float) sin(toRadians(movementAngle - 270)) * 5
        );
    }

    @Override
    public void update() {
        if (movingForward) {
            moveForward();
        }
    }

    public void render() {
        List<Transformation> transformations = of(
                scale(getPosition(), .01f, .01f, .01f),
                translate(vertex(getPosition().getX(), getPosition().getY() + 100, getPosition().getZ())),
                rotate(vertex(0, 1, 0), movementAngle)
        );

        renderFrom(model, color, transformations);
    }
}
