package scene;

import entity.Building;
import entity.Entity;
import entity.Movable;
import entity.Player;
import entity.Road;
import entity.Terrain;
import geometry.TriangleMesh;
import geometry.Vector;
import geometry.Vertex;
import model.BuildingModel;
import model.MapEntity;
import model.MapStructure;
import render.TextureManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static geometry.Vector.vector;
import static geometry.Vertex.vertex;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static model.MapEntity.BUILDING_RESOURCE_NAME;
import static model.MapEntity.ROAD_RESOURCE_NAME;
import static model.MapEntity.TERRAIN_RESOURCE_NAME;
import static render.TextureManager.getTexture;
import static scene.World.WORLD_WIDTH;
import static scene.World.X_LOWER_BOUND;
import static scene.World.Z_LOWER_BOUND;
import static util.FileUtils.deserializeFrom;

public class SceneManager {

    private final Camera camera;
    private final List<Entity> entities;

    public SceneManager(Camera camera, String mapStructureFilePath, String buildingsFilePath) {
        entities = new ArrayList<>();
        MapStructure mapStructure = deserializeFrom(mapStructureFilePath, MapStructure.class);
        BuildingModel[] buildingModels = deserializeFrom(buildingsFilePath, BuildingModel[].class);
        initializeStaticEntities(mapStructure, buildingModels);
        this.camera = camera;
    }

    public Player getPlayer() {
        return (Player) entities
                .stream()
                .filter(entity -> entity.getClass() == Player.class)
                .findFirst().orElseThrow(() -> new IllegalStateException("Player is not instantiated"));
    }

    public void moveCameraForward() {
        Vertex cameraPosition = camera.getPosition();
        Vertex cameraTarget = camera.getTarget();
        Vector cameraTargetVector = vector(cameraPosition, cameraTarget);
        Vector cameraTargetVersor = cameraTargetVector.versor();
        camera.setPosition(vertex(
                cameraPosition.getX() + cameraTargetVersor.getX(),
                cameraPosition.getY(),
                cameraPosition.getZ() + cameraTargetVersor.getZ()
        ));
        camera.setTarget(vertex(
                cameraTarget.getX() + cameraTargetVersor.getX(),
                cameraTarget.getY(),
                cameraTarget.getZ() + cameraTargetVersor.getZ()
        ));
    }

    public void movePlayerForward() {
        getPlayer().beginMoveForward();
    }

    public void movePlayerLeft() {
        getPlayer().moveLeft();
    }

    public void movePlayerRight() {
        getPlayer().moveRight();
    }

    public Camera getCamera() {
        return camera;
    }

    public void render() {
        entities.forEach(Entity::render);
    }

    public void update() {
        getMovableEntities().forEach(Movable::update);
    }

    private void initializeStaticEntities(MapStructure mapStructure, BuildingModel[] buildingModels) {
        MapEntity terrain = mapStructure.getMapEntityBy(TERRAIN_RESOURCE_NAME);
        MapEntity road = mapStructure.getMapEntityBy(ROAD_RESOURCE_NAME);
        MapEntity building = mapStructure.getMapEntityBy(BUILDING_RESOURCE_NAME);

        initializeTexturesFor(buildingModels, terrain, road);

        createPlayer();

        int[] description = mapStructure.getDescription();
        for (int i = 0, descriptionLength = description.length; i < descriptionLength; i++) {
            int cell = description[i];
            int xPos = (i / WORLD_WIDTH) + X_LOWER_BOUND;
            int zPos = (i % WORLD_WIDTH) + Z_LOWER_BOUND;
            Vertex position = vertex(xPos, 1, zPos);
            if (cell == terrain.getId()) {
                entities.add(new Terrain(position, getTexture(TERRAIN_RESOURCE_NAME)));
            } else if (cell == road.getId()) {
                entities.add(new Road(position, getTexture(ROAD_RESOURCE_NAME)));
            } else if (cell == building.getId()) {
                entities.add(spawnRandomBuildingAt(position, buildingModels));
            }
        }
    }

    private void initializeTexturesFor(BuildingModel[] buildingModels, MapEntity terrain, MapEntity road) {
        TextureManager.addTexture(TERRAIN_RESOURCE_NAME, terrain.getTextureResource());
        TextureManager.addTexture(ROAD_RESOURCE_NAME, road.getTextureResource());
        stream(buildingModels)
                .forEach(buildingModel -> TextureManager.addTexture(
                        buildingModel.getTextureResource(),
                        buildingModel.getTextureResource()
                ));
    }

    private Building spawnRandomBuildingAt(Vertex position, BuildingModel[] buildingModels) {
        BuildingModel randomBuildingModel = buildingModels[new Random().nextInt(buildingModels.length)];

        return new Building(
                position,
                TextureManager.getTexture(randomBuildingModel.getTextureResource()),
                randomBuildingModel.getHeight());
    }

    private void createPlayer() {
        entities.add(new Player(vertex(0, 1, 0), TriangleMesh.loadFromTRI("car2.tri")));
    }

    private List<Movable> getMovableEntities() {
        return entities
                .stream()
                .filter(entity -> entity instanceof Movable)
                .map(Movable.class::cast)
                .collect(toList());
    }
}
