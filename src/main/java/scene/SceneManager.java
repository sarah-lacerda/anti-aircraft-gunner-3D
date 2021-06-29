package scene;

import entity.Building;
import entity.Entity;
import entity.Gas;
import entity.Movable;
import entity.Player;
import entity.Road;
import entity.Terrain;
import entity.enemy.Enemy;
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
import java.util.stream.Collectors;

import static entity.Gas.TOTAL_AMOUNT_OF_GAS_CONTAINERS;
import static entity.Player.PLAYER_SPAWN_POSITION;
import static entity.enemy.Enemy.TOTAL_ENEMIES;
import static geometry.Vertex.vertex;
import static java.util.Arrays.stream;
import static java.util.stream.IntStream.range;
import static model.MapEntity.BUILDING_RESOURCE_NAME;
import static model.MapEntity.ROAD_RESOURCE_NAME;
import static model.MapEntity.TERRAIN_RESOURCE_NAME;
import static render.TextureManager.getTexture;
import static scene.World.WORLD_WIDTH;
import static scene.World.X_LOWER_BOUND;
import static scene.World.Z_LOWER_BOUND;
import static util.Color.DARK_GREEN;
import static util.Color.FIREBRICK;
import static util.Color.SADDLE_BROWN;
import static util.Color.TEAL;
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
                        String gasModelFilepath,
                        String enemyModelFilepath) {
        entities = new ArrayList<>();
        MapStructure mapStructure = deserializeFrom(mapStructureFilepath, MapStructure.class);
        BuildingModel[] buildingModels = deserializeFrom(buildingsFilepath, BuildingModel[].class);
        initializeEntities(mapStructure, buildingModels, playerModelFilepath, gasModelFilepath, enemyModelFilepath);
        this.camera = camera;
        this.lastTimeCameraViewWasToggled = getCurrentTimeInSeconds();
    }

    public Player getPlayer() {
        return (Player) entities
                .stream()
                .filter(entity -> entity.getClass() == Player.class)
                .findFirst().orElseThrow(() -> new IllegalStateException("Player is not instantiated"));
    }

    public List<Movable> getMovableEntities() {
        return entities
                .stream()
                .filter(entity -> entity instanceof Movable)
                .map(Movable.class::cast)
                .collect(Collectors.toUnmodifiableList());
    }

    public List<Enemy> getEnemies() {
        return entities
                .stream()
                .filter(entity -> entity instanceof Enemy)
                .map(Enemy.class::cast)
                .collect(Collectors.toUnmodifiableList());
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

    public List<Entity> getEntities() {
        return entities;
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public boolean removeEntity(Entity entity) {
        return entities.remove(entity);
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

    public void destroyEntitiesOutOfReach() {
        entities.removeIf(entity -> entity.getPosition().getY() < 0);
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

    private void initializeEntities(MapStructure mapStructure,
                                    BuildingModel[] buildingModels,
                                    String playerModelFilepath,
                                    String gasModelFilepath,
                                    String enemyModelFilepath) {
        MapEntity terrain = mapStructure.getMapEntityBy(TERRAIN_RESOURCE_NAME);
        MapEntity road = mapStructure.getMapEntityBy(ROAD_RESOURCE_NAME);
        MapEntity building = mapStructure.getMapEntityBy(BUILDING_RESOURCE_NAME);

        initializeTexturesFor(buildingModels, terrain, road);
        createPlayer(playerModelFilepath);
        initializeStaticStructures(mapStructure, buildingModels, terrain, road, building);
        spawnGasContainersAtRandomPlaces(gasModelFilepath);
        spawnEnemies(enemyModelFilepath);
    }

    private void spawnEnemies(String enemyModelFilepath) {
        List<Color> colors = List.of(SADDLE_BROWN, DARK_GREEN);
        range(0, TOTAL_ENEMIES)
                .mapToObj(i -> new Enemy(TriangleMesh.loadFromTRI(enemyModelFilepath), colors.get(i % colors.size()), i))
                .forEach(entities::add);
    }

    private void initializeStaticStructures(MapStructure mapStructure,
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
        range(0, TOTAL_AMOUNT_OF_GAS_CONTAINERS)
                .mapToObj(i -> randomRoadTile()).map(randomRoadTitle -> new Gas(
                vertex(randomRoadTitle.getPosition().getX(), 1.5f, randomRoadTitle.getPosition().getZ()),
                modelMesh,
                FIREBRICK
        )).forEach(entities::add);
    }

    private Road randomRoadTile() {
        Entity entity;
        do {
            entity = entities.get(randomIntWithinRange(0, entities.size()));
        } while (entity.getClass() != Road.class);
        return (Road) entity;
    }

    private void createPlayer(String modelFilepath) {
        entities.add(new Player(PLAYER_SPAWN_POSITION, TriangleMesh.loadFromTRI(modelFilepath), TEAL));
    }

}
