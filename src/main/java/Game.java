import glfw.Window;
import scene.Camera;
import scene.SceneManager;

import static de.damios.guacamole.gdx.StartOnFirstThreadHelper.startNewJvmIfRequired;
import static model.BuildingModel.BUILDINGS_FILEPATH;
import static model.MapStructure.MAP_STRUCTURE_FILEPATH;
import static render.Renderer.enable3D;
import static render.Texture.enableTextures;

public class Game {

    private static final Window WINDOW = Window.getInstance();

    public static void main(String[] args) {
        // Starts a new JVM if the application was started on macOS without the
        // -XstartOnFirstThread argument.
        if (startNewJvmIfRequired()) {
            System.exit(0);
        }
        enable3D();
        enableTextures();
        SceneManager sceneManager = new SceneManager(new Camera(), MAP_STRUCTURE_FILEPATH, BUILDINGS_FILEPATH);
        WINDOW.setSceneManager(sceneManager);
        WINDOW.run();
    }
}
