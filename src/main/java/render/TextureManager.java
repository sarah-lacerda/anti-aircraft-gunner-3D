package render;

import java.util.HashMap;
import java.util.Map;

public class TextureManager {

    private static final Map<String, Texture> textures = new HashMap<>();

    private TextureManager() {}

    public static void addTexture(String name, String filePath) {
        addTexture(name, Texture.loadTextureFrom(filePath));
    }
    public static void addTexture(String name, Texture texture) {
        textures.put(name, texture);
    }

    public static Texture getTexture(String name) {
        return textures.get(name);
    }
}
