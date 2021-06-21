package model;

import java.util.List;

public class MapStructure {

    private long height;
    private long width;
    private int[] description;
    private List<MapEntity> mapEntities;

    public static final String MAP_STRUCTURE_FILEPATH = "testMap.json";

    public long getHeight() {
        return height;
    }

    public long getWidth() {
        return width;
    }

    public int[] getDescription() {
        return description;
    }

    public List<MapEntity> getMapEntities() {
        return mapEntities;
    }

    public MapEntity getMapEntityBy(String name) {
        return mapEntities
                .stream()
                .filter(mapEntity -> mapEntity.getName().equals(name))
                .findFirst()
                .orElseThrow();
    }
}
