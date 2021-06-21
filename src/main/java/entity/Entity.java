package entity;

import geometry.Vertex;

public abstract class Entity {
    private Vertex position;

    public Entity(Vertex position) {
        this.position = position;
    }

    public Vertex getPosition() {
        return position;
    }

    public abstract void render();
}
