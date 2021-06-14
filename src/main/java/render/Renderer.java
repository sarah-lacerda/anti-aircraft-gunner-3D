package render;


import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glVertex3f;

public class Renderer {

    private static float rotation = 0;

    public static void enable3D() {
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
    }

    public static void renderSampleCube() {
        glRotatef(rotation++, 0, 0, 1);
        glBegin(GL_QUADS);
        // Front Face
        glNormal3f(0, 0, 1);
        glColor3f(1f, 0, 0); // RED
        glVertex3f(-1.0f, -1.0f, 1.0f);
        glVertex3f(1.0f, -1.0f, 1.0f);
        glVertex3f(1.0f, 1.0f, 1.0f);
        glVertex3f(-1.0f, 1.0f, 1.0f);
        glEnd();

        glBegin(GL_QUADS);
        // Back Face
        glNormal3f(0, 0, -1);
        glColor3f(0, 1f, 0); // GREEN
        glVertex3f(-1.0f, -1.0f, -1.0f);
        glVertex3f(-1.0f, 1.0f, -1.0f);
        glVertex3f(1.0f, 1.0f, -1.0f);
        glVertex3f(1.0f, -1.0f, -1.0f);
        glEnd();

        glBegin(GL_QUADS);
        // Top Face
        glNormal3f(0, 1, 0);
        glColor3f(0, 0, 1f); // BLUE
        glVertex3f(-1.0f, 1.0f, -1.0f);
        glVertex3f(-1.0f, 1.0f, 1.0f);
        glVertex3f(1.0f, 1.0f, 1.0f);
        glVertex3f(1.0f, 1.0f, -1.0f);
        glEnd();

        glBegin(GL_QUADS);
        // Bottom Face
        glNormal3f(0, -1, 0);
        glColor3f(1f, 1f, 0); // Yellow
        glVertex3f(-1.0f, -1.0f, -1.0f);
        glVertex3f(1.0f, -1.0f, -1.0f);
        glVertex3f(1.0f, -1.0f, 1.0f);
        glVertex3f(-1.0f, -1.0f, 1.0f);
        glEnd();

        glBegin(GL_QUADS);
        // Right face
        glNormal3f(1, 0, 0);
        glColor3f(0, 1f, 1f); // Light blue
        glVertex3f(1.0f, -1.0f, -1.0f);
        glVertex3f(1.0f, 1.0f, -1.0f);
        glVertex3f(1.0f, 1.0f, 1.0f);
        glVertex3f(1.0f, -1.0f, 1.0f);
        glEnd();

        glBegin(GL_QUADS);
        // Left Face
        glNormal3f(-1, 0, 0);
        glColor3f(1f, 0, 1f); // Pink
        glVertex3f(-1.0f, -1.0f, -1.0f);
        glVertex3f(-1.0f, -1.0f, 1.0f);
        glVertex3f(-1.0f, 1.0f, 1.0f);
        glVertex3f(-1.0f, 1.0f, -1.0f);
        glEnd();
    }
}
