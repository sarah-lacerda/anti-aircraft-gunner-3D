package model;

public class MapEntity {
    private String name;
    private int id;
    private String textureResource;

    public static final String TERRAIN_RESOURCE_NAME = "terrain";
    public static final String ROAD_RESOURCE_NAME = "road";
    public static final String BUILDING_RESOURCE_NAME = "building";

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getTextureResource() {
        return textureResource;
    }
}
