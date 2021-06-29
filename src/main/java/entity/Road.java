package entity;

import geometry.Vertex;
import render.Texture;

import static render.Renderer.renderCellAt;

public class Road extends Entity {

    private final Texture texture;
    private final HitBox hitBox;

    public Road(Vertex position, Texture texture) {
        super(position);
        this.texture = texture;
        this.hitBox = new HitBox(1, 1, 1);
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
