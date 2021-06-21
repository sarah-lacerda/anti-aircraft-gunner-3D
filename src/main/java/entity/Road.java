package entity;

import geometry.Vertex;
import render.Texture;

import static render.Renderer.renderCellAt;

public class Road extends Entity {

    private final Texture texture;

    public Road(Vertex position, Texture texture) {
        super(position);
        this.texture = texture;
    }

    @Override
    public void render() {
        renderCellAt(getPosition(), texture);
    }
}
