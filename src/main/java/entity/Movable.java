package entity;

import geometry.Vertex;

public abstract class Movable extends Entity {
    public Movable(Vertex position) {
        super(position);
    }

    public abstract void update();
}
