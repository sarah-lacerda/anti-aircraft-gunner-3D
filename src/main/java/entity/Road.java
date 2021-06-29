package entity;

import geometry.Vertex;
import render.Texture;

import static render.Renderer.renderCellAt;

public class Road extends Entity {

    private final Texture texture;
    private final HitBox hitBox;
    private boolean damaged;

    private static final Texture DAMAGED_TEXTURE = Texture.loadTextureFrom("damaged.png");

    public Road(Vertex position, Texture texture) {
        super(position);
        this.texture = texture;
        this.hitBox = new HitBox(1, 1, 1);
        this.damaged = false;
    }

    public boolean isDamaged() {
        return damaged;
    }

    public void damage() {
        damaged = true;
    }

    @Override
    public HitBox getHitBox() {
        return hitBox;
    }

    @Override
    public void render() {
        if (damaged) {
            renderCellAt(getPosition(), DAMAGED_TEXTURE);
        } else {
            renderCellAt(getPosition(), texture);
        }
    }
}
