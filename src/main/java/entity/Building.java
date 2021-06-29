package entity;

import geometry.Vertex;
import render.Texture;

import java.util.Collections;

import static render.Renderer.renderDebug;
import static render.Renderer.renderTexturedParallelepiped;
import static render.transformation.Transformation.translate;
import static util.Debug.DEBUG_ENABLED;

public class Building extends Entity {

    private final Texture texture;
    private final float height;
    private final HitBox hitBox;

    public Building(Vertex position, Texture texture, float height) {
        super(position);
        this.texture = texture;
        this.height = height;
        this.hitBox = new HitBox(height, 2, 2);
    }

    @Override
    public HitBox getHitBox() {
        return hitBox;
    }

    @Override
    public void render() {
        renderTexturedParallelepiped(texture, getPosition(), 1, height, 1);

        if (DEBUG_ENABLED) {
            renderDebug(hitBox, Collections.singletonList(translate(getPosition())));
        }
    }
}
