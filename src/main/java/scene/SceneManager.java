package scene;

import entity.Building;
import entity.Entity;
import entity.Gas;
import entity.Movable;
import entity.Player;
import entity.Road;
import entity.Terrain;
import geometry.TriangleMesh;
import geometry.Vertex;
import model.BuildingModel;
import model.MapEntity;
import model.MapStructure;
import render.TextureManager;
import util.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static entity.Gas.TOTAL_AMOUNT_OF_GAS_CONTAINERS;
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
import static util.Randomizer.randomIntWithinRange;
import static util.Time.deltaTimeInSecondsFrom;
import static util.Time.getCurrentTimeInSeconds;

public class SceneManager {

    private final Camera camera;
    private final List<Entity> entities;
    private double lastTimeCameraViewWasToggled;

    private static final double CAMERA_VIEW_TOGGLE_COOLDOWN_IN_SECONDS = .25;

    public SceneManager(Camera camera,
                        String mapStructureFilepath,
                        String buildingsFilepath,
                        String playerModelFilepath,
                        String gasModelFilepath) {
        entities = new ArrayList<>();
        MapStructure mapStructure = deserializeFrom(mapStructureFilepath, MapStructure.class);
        BuildingModel[] buildingModels = deserializeFrom(buildingsFilepath, BuildingModel[].class);
        initializeStaticEntities(mapStructure, buildingModels, playerModelFilepath, gasModelFilepath);
        this.camera = camera;
        this.lastTimeCameraViewWasToggled = getCurrentTimeInSeconds();
    }

    public Player getPlayer() {
        return (Player) entities
                .stream()
                .filter(entity -> entity.getClass() == Player.class)
                .findFirst().orElseThrow(() -> new IllegalStateException("Player is not instantiated"));
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
        setFirstPersonViewCameraValuesIfActive(camera.isPerspectiveViewEnabled());
        camera.update();
    }

    public void toggleCameraView() {
        setFirstPersonViewCameraValuesIfActive(
                isCooldownForToggleCameraViewExpired() && camera.toggleCameraPerspective()
        );
        if (isCooldownForToggleCameraViewExpired()) {
            lastTimeCameraViewWasToggled = getCurrentTimeInSeconds();
        }
    }

    private boolean isCooldownForToggleCameraViewExpired() {
        return deltaTimeInSecondsFrom(lastTimeCameraViewWasToggled) > CAMERA_VIEW_TOGGLE_COOLDOWN_IN_SECONDS;
    }

    private void setFirstPersonViewCameraValuesIfActive(boolean firstPersonCameraActive) {
        if (firstPersonCameraActive) {
            camera.setPosition(getPlayer().getFirstPersonCameraPosition());
            camera.setTarget(getPlayer().getFirstPersonCameraTarget());
        }
    }

    private void initializeStaticEntities(MapStructure mapStructure,
                                          BuildingModel[] buildingModels,
                                          String playerModelFilepath,
                                          String gasModelFilepath) {
        MapEntity terrain = mapStructure.getMapEntityBy(TERRAIN_RESOURCE_NAME);
        MapEntity road = mapStructure.getMapEntityBy(ROAD_RESOURCE_NAME);
        MapEntity building = mapStructure.getMapEntityBy(BUILDING_RESOURCE_NAME);

        initializeTexturesFor(buildingModels, terrain, road);

        createPlayer(playerModelFilepath);

        createMapAndBuildings(mapStructure, buildingModels, terrain, road, building);

        spawnGasContainersAtRandomPlaces(gasModelFilepath);
    }

    private void createMapAndBuildings(MapStructure mapStructure,
                                       BuildingModel[] buildingModels,
                                       MapEntity terrain,
                                       MapEntity road,
                                       MapEntity building) {
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

    private void spawnGasContainersAtRandomPlaces(String gasModelFilepath) {
        TriangleMesh modelMesh = TriangleMesh.loadFromTRI(gasModelFilepath);
        for (int i = 0; i < TOTAL_AMOUNT_OF_GAS_CONTAINERS; i++) {
            Road randomRoadTitle = randomRoadTile();
            entities.add(
                    new Gas(
                            vertex(randomRoadTitle.getPosition().getX(), 1.5f, randomRoadTitle.getPosition().getZ()),
                            modelMesh,
                            Color.FIREBRICK
                    )
            );
        }
    }

    private Road randomRoadTile() {
        Entity entity;
        do {
            entity = entities.get(randomIntWithinRange(0, entities.size()));
        } while (entity.getClass() != Road.class);
        return (Road) entity;
    }

    private void createPlayer(String modelFilepath) {
        entities.add(new Player(vertex(0, 1, 0), TriangleMesh.loadFromTRI(modelFilepath), Color.TEAL));
    }

    private List<Movable> getMovableEntities() {
        return entities
                .stream()
                .filter(entity -> entity instanceof Movable)
                .map(Movable.class::cast)
                .collect(toList());
    }
}
