package entity;

import geometry.Vertex;
import model.MapEntity;
import model.MapStructure;
import render.TextureManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static geometry.Vertex.vertex;
import static model.MapEntity.BUILDING_RESOURCE_NAME;
import static model.MapEntity.ROAD_RESOURCE_NAME;
import static model.MapEntity.TERRAIN_RESOURCE_NAME;
import static render.TextureManager.getTexture;
import static scene.World.WORLD_WIDTH;
import static scene.World.X_LOWER_BOUND;
import static scene.World.Z_LOWER_BOUND;

public class City {
    private final List<Building> buildings;

    private Map<Vertex, Entity> staticEntities;

    public City(MapStructure mapStructure, List<Building> buildings) {
        this.staticEntities = createStaticEntities(mapStructure);
        this.buildings = buildings;
    }

    public void render() {
        staticEntities.values().forEach(Entity::render);
    }

    private Map<Vertex, Entity> createStaticEntities(MapStructure mapStructure) {
        MapEntity terrain = mapStructure.getMapEntityBy(TERRAIN_RESOURCE_NAME);
        MapEntity road = mapStructure.getMapEntityBy(ROAD_RESOURCE_NAME);
        MapEntity building = mapStructure.getMapEntityBy(BUILDING_RESOURCE_NAME);
        HashMap<Vertex, Entity> staticEntities = new HashMap<>();
        TextureManager.addTexture(TERRAIN_RESOURCE_NAME, terrain.getTextureResource());
        TextureManager.addTexture(ROAD_RESOURCE_NAME, road.getTextureResource());

        int[] description = mapStructure.getDescription();
        for (int i = 0, descriptionLength = description.length; i < descriptionLength; i++) {
            int cell = description[i];
            int xPos = (i / WORLD_WIDTH) + X_LOWER_BOUND;
            int zPos = (i % WORLD_WIDTH) + Z_LOWER_BOUND;
            Vertex position = vertex(xPos, 1, zPos);
            if (cell == terrain.getId()) {
                staticEntities.put(position, new Terrain(position , getTexture(TERRAIN_RESOURCE_NAME)));
            } else if (cell == road.getId()) {
                staticEntities.put(position, new Road(position, getTexture(ROAD_RESOURCE_NAME)));
            } else if (cell == building.getId()) {
                staticEntities.put(position, spawnRandomBuilding());
            }
        }
        return staticEntities;
    }

    private Building spawnRandomBuilding() {
        return buildings.get(new Random().nextInt(buildings.size()));
    }
}
