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
import static render.Renderer.FRAMES_PER_SECOND;
import static render.Renderer.renderDebug;
import static render.Renderer.renderFrom;
import static render.transformation.Transformation.rotate;
import static render.transformation.Transformation.scale;
import static render.transformation.Transformation.translate;
import static scene.World.WORLD_WIDTH;
import static scene.World.X_LOWER_BOUND;
import static scene.World.X_UPPER_BOUND;
import static scene.World.Z_LOWER_BOUND;
import static scene.World.Z_UPPER_BOUND;
import static util.Debug.DEBUG_ENABLED;

public class Player extends Movable {

    private final TriangleMesh model;
    private final Color color;
    private final HitBox hitBox;
    private boolean movingForward;
    private float movementAngle = 0;

    private final static float SPEED = 1f / 20f;
    private static final float UNIT_OF_MOVEMENT_PER_FRAME = (WORLD_WIDTH / (float) FRAMES_PER_SECOND) * SPEED;
    public static final String PLAYER_MODEL_FILEPATH = "car2.tri";
    public static final Vertex PLAYER_SPAWN_POSITION = vertex(-42.0f, 1.0f, 45.0f);

    public Player(Vertex position, TriangleMesh model, Color color) {
        super(position);
        this.model = model;
        this.color = color;
        this.movingForward = false;
        hitBox = new HitBox(2f, 1f, 2f);
    }

    @Override
    public void setPosition(Vertex position) {
        if (isValid(position)) {
            super.setPosition(position);
        }
    }

    public void moveLeft() {
        movementAngle += 1.0f;
    }

    public void moveRight() {
        movementAngle -= 1.0f;
    }

    public void setMovingForward(boolean movingForward) {
        this.movingForward = movingForward;
    }

    public void beginMoveForward() {
        if (!movingForward) {
            moveForward();
        }
    }

    public void moveBackwards() {
        setPosition(vertex(
                getPosition().getX() -
                        (float) cos(toRadians(movementAngle - 270)) *
                                UNIT_OF_MOVEMENT_PER_FRAME,
                getPosition().getY()
                ,
                getPosition().getZ() +
                        (float) sin(toRadians(movementAngle - 270)) *
                                UNIT_OF_MOVEMENT_PER_FRAME
                )
        );
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

    @Override
    public HitBox getHitBox() {
        return hitBox;
    }

    public void render() {
        List<Transformation> transformations = List.of(
                scale(getPosition(), .01f, .01f, .01f),
                translate(vertex(getPosition().getX(), getPosition().getY() + 100, getPosition().getZ())),
                rotate(vertex(0, 1, 0), movementAngle)
        );

        renderFrom(model, color, transformations);

        if (DEBUG_ENABLED) {
            List<Transformation> hitBoxTransformations = List.of(
                    translate(vertex(getPosition().getX(), getPosition().getY(), getPosition().getZ())),
                    rotate(vertex(0, 1, 0), movementAngle)
            );
            renderDebug(hitBox, hitBoxTransformations);
        }
    }

    private boolean isValid(Vertex position) {
        return position.getX() > X_LOWER_BOUND &&
                position.getX() < X_UPPER_BOUND &&
                position.getZ() > Z_LOWER_BOUND &&
                position.getZ() < Z_UPPER_BOUND;
    }
}
