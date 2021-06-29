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

    public void setPosition(Vertex position) {
        this.position = position;
    }

    public abstract HitBox getHitBox();

    public abstract void render();
}
