package entity;

import geometry.TriangleMesh;
import geometry.Vertex;
import util.Color;

import static geometry.Vertex.vertex;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;
import static render.Renderer.FRAMES_PER_SECOND;
import static render.Renderer.renderFrom;
import static scene.World.WORLD_WIDTH;

public class Player extends Movable {

    private final TriangleMesh model;
    private final Color color;
    private boolean movingForward;
    private float movementAngle = 0;

    private static final float UNIT_OF_MOVEMENT_PER_FRAME = (WORLD_WIDTH / (float) FRAMES_PER_SECOND) * 5;

    public Player(Vertex position, TriangleMesh model) {
        super(position);
        this.model = model;
        this.color = Color.TEAL;
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

    @Override
    public void update() {
        if (movingForward) {
            moveForward();
        }
    }

    public void render() {
        renderFrom(model,
                vertex(getPosition().getX(), getPosition().getY() + 200f, getPosition().getZ()),
                color,
                .01f,
                movementAngle
        );
    }
}
