package render;

import de.matthiasmann.twl.utils.PNGDecoder;
import exception.FailedToLoadResourceException;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNPACK_ALIGNMENT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static util.FileUtils.getFileFromResourceAsStream;

public class Texture {

    private final int id;

    private Texture(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public static void enableTextures() {
        glEnable(GL_TEXTURE_2D);
    }

    public static void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public static Texture loadTextureFrom(String filePath) {
        try {
            //load png file
            final PNGDecoder decoder = new PNGDecoder(getFileFromResourceAsStream(filePath));

            //create a byte buffer big enough to store RGBA values
            ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());

            //decode
            decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);

            //flip the buffer so its ready to read
            buffer.flip();

            //create a texture
            final int id = glGenTextures();

            //bind the texture
            glBindTexture(GL_TEXTURE_2D, id);

            //tell opengl how to unpack bytes
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

            //set the texture parameters, can be GL_LINEAR or GL_NEAREST
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

            //upload texture
            glTexImage2D(
                    GL_TEXTURE_2D,
                    0,
                    GL_RGBA,
                    decoder.getWidth(),
                    decoder.getHeight(),
                    0,
                    GL_RGBA,
                    GL_UNSIGNED_BYTE,
                    buffer
            );

            // Generate Mip Map
            glGenerateMipmap(GL_TEXTURE_2D);

            // Unbind texture
            glBindTexture(GL_TEXTURE_2D, 0);
            return new Texture(id);
        } catch (IOException e) {
            throw new FailedToLoadResourceException("Texture", filePath, e);
        }
    }
}
