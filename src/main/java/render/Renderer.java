package render;


import geometry.Triangle;
import geometry.TriangleMesh;
import geometry.Vertex;
import util.Color;

import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static util.Color.WHITE;

public class Renderer {

    public static final int FRAMES_PER_SECOND = 30;

    private Renderer() {}

    public static void enable3D() {
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
    }

    public static boolean canRender(double elapsedTimeSinceLastRendering) {
        return elapsedTimeSinceLastRendering > 1.0f / FRAMES_PER_SECOND;
    }

    public static void renderFrom(TriangleMesh triangleMesh,
                                  Vertex atPosition,
                                  Color withColor,
                                  float scaleFactor,
                                  float rotationAngle) {
        glPushMatrix();
        glScalef(scaleFactor, scaleFactor, scaleFactor);
        glTranslatef(atPosition.getX(), atPosition.getY(), atPosition.getZ());
        glRotatef(rotationAngle, 0, 1, 0);
        glBegin(GL_TRIANGLES);
        glColor3f(withColor.getRed(), withColor.getGreen(), withColor.getBlue());
        for (Triangle triangle : triangleMesh.getTriangles()) {
            Vertex a = triangle.getA();
            Vertex b = triangle.getB();
            Vertex c = triangle.getC();

            glVertex3f(a.getX(), a.getY(), a.getZ());
            glVertex3f(b.getX(), b.getY(), b.getZ());
            glVertex3f(c.getX(), c.getY(), c.getZ());
        }
        glEnd();
        glPopMatrix();
    }

    public static void renderCellAt(Vertex position, Texture withTexture) {
        withTexture.bind();
        glPushMatrix();
        setColor(WHITE);
        glTranslatef(position.getX(), position.getY(), position.getZ());
        glBegin(GL_QUADS);
        glTexCoord2f(0, 1);
        glVertex3f(0, 0, 0);
        glTexCoord2f(1, 1);
        glVertex3f(0, 0, 1.0f);
        glTexCoord2f(1, 0);
        glVertex3f(1.0f, 0, 1.0f);
        glTexCoord2f(0, 0);
        glVertex3f(1.0f, 0, 0);
        glEnd();
        glPopMatrix();
        Texture.unbind();
    }

    public static void renderTexturedParallelepiped(Texture texture,
                                                    Vertex atPosition,
                                                    float xSize,
                                                    float ySize,
                                                    float zSize
    ) {
        glPushMatrix();
        setColor(WHITE);
        glScalef(xSize, ySize, zSize);
        glTranslatef(atPosition.getX(),atPosition.getY(),atPosition.getZ());
        glEnable(GL_TEXTURE_2D);
        texture.bind();

        glBegin(GL_QUADS);
        // Front Face
        glTexCoord2f(0, 1);
        glVertex3f(-1.0f, -1.0f, 1.0f);
        glTexCoord2f(1, 1);
        glVertex3f(1.0f, -1.0f, 1.0f);
        glTexCoord2f(1, 0);
        glVertex3f(1.0f, 1.0f, 1.0f);
        glTexCoord2f(0, 0);
        glVertex3f(-1.0f, 1.0f, 1.0f);

        // Back Face
        glTexCoord2f(0, 1);
        glVertex3f(-1.0f, -1.0f, -1.0f);
        glTexCoord2f(1, 1);
        glVertex3f(-1.0f, 1.0f, -1.0f);
        glTexCoord2f(1, 0);
        glVertex3f(1.0f, 1.0f, -1.0f);
        glTexCoord2f(0, 0);
        glVertex3f(1.0f, -1.0f, -1.0f);

        // Top Face
        glVertex3f(-1.0f, 1.0f, -1.0f);
        glVertex3f(-1.0f, 1.0f, 1.0f);
        glVertex3f(1.0f, 1.0f, 1.0f);
        glVertex3f(1.0f, 1.0f, -1.0f);

        // Bottom Face
        glVertex3f(-1.0f, -1.0f, -1.0f);
        glVertex3f(1.0f, -1.0f, -1.0f);
        glVertex3f(1.0f, -1.0f, 1.0f);
        glVertex3f(-1.0f, -1.0f, 1.0f);

        // Right face
        glTexCoord2f(0, 1);
        glVertex3f(1.0f, -1.0f, -1.0f);
        glTexCoord2f(1, 1);
        glVertex3f(1.0f, 1.0f, -1.0f);
        glTexCoord2f(1, 0);
        glVertex3f(1.0f, 1.0f, 1.0f);
        glTexCoord2f(0, 0);
        glVertex3f(1.0f, -1.0f, 1.0f);

        // Left Face
        glTexCoord2f(0, 1);
        glVertex3f(-1.0f, -1.0f, -1.0f);
        glTexCoord2f(1, 1);
        glVertex3f(-1.0f, -1.0f, 1.0f);
        glTexCoord2f(1, 0);
        glVertex3f(-1.0f, 1.0f, 1.0f);
        glTexCoord2f(0, 0);
        glVertex3f(-1.0f, 1.0f, -1.0f);
        glEnd();
        glPopMatrix();
        Texture.unbind();
    }

    private static void setColor(Color color) {
        glColor3f(color.getRed(), color.getGreen(), color.getBlue());
    }
}
