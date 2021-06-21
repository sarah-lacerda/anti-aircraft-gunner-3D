package entity;

import geometry.Vertex;
import render.Texture;

import static render.Renderer.renderTexturedParallelepiped;

public class Building extends Entity {

    private final Texture texture;
    private final float height;

    public Building(Vertex position, Texture texture, float height) {
        super(position);
        this.texture = texture;
        this.height = height;
    }

    @Override
    public void render() {
        renderTexturedParallelepiped(texture, getPosition(), 1, height, 1);
    }
}
