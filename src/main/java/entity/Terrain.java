package entity;

import geometry.Vertex;
import render.Texture;

import static render.Renderer.renderCellAt;

public class Terrain extends Entity {

    private final Texture texture;
    private final HitBox hitBox;

    public Terrain(Vertex position, Texture texture) {
        super(position);
        this.texture = texture;
        this.hitBox = new HitBox(1, .8f, .8f);
    }

    @Override
    public HitBox getHitBox() {
        return hitBox;
    }

    @Override
    public void render() {
        renderCellAt(getPosition(), texture);
    }
}
