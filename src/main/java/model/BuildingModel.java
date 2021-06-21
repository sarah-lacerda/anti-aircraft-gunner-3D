package model;

public class BuildingModel {
    private String textureResource;
    private int height;

    public static final String BUILDINGS_FILEPATH = "buildings.json";

    public String getTextureResource() {
        return textureResource;
    }

    public int getHeight() {
        return height;
    }
}
