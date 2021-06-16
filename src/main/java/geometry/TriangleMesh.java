package geometry;

import exception.FailedToLoadResourceException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Float.parseFloat;
import static util.FileUtils.getBufferedReaderForFile;

public class TriangleMesh {
    List<Triangle> triangles;

    public TriangleMesh(List<Triangle> triangles) {
        this.triangles = triangles;
    }

    public TriangleMesh() {
        this.triangles = new ArrayList<>();
    }

    public void addTriangle(Triangle triangle) {
        triangles.add(triangle);
    }

    public List<Triangle> getTriangles() {
        return triangles;
    }

    public static TriangleMesh loadFrom(String filepath) {
        if (!filepath.endsWith(".tri")) {
            throw new FailedToLoadResourceException("TriangleMesh", filepath, "Not a TRI file!");
        }
        try {
            BufferedReader reader = getBufferedReaderForFile(filepath);
            String line = getFirstEntry(reader);
            TriangleMesh triangleMesh = new TriangleMesh();
            while (line != null) {
                final String[] lineContents = line.split("\\s+");
                final Vertex a = new Vertex(
                        parseFloat(lineContents[0]),
                        parseFloat(lineContents[1]),
                        parseFloat(lineContents[2])
                );
                final Vertex b = new Vertex(
                        parseFloat(lineContents[3]),
                        parseFloat(lineContents[4]),
                        parseFloat(lineContents[5])
                );
                final Vertex c = new Vertex(parseFloat(lineContents[6]),
                        parseFloat(lineContents[7]),
                        parseFloat(lineContents[8])
                );
                triangleMesh.addTriangle(new Triangle(a, b, c));
                line = reader.readLine();
            }
            return triangleMesh;
        } catch (IOException e) {
            throw new FailedToLoadResourceException("TriangleMesh", filepath, e);
        } catch (NumberFormatException e) {
            throw new FailedToLoadResourceException(
                    "TriangleMesh",
                    filepath,
                    "File is corrupted or does not match TRI format!"
            );
        }
    }

    private static String getFirstEntry(BufferedReader reader) throws IOException {
        String firstLine = reader.readLine();
        if (firstLine.split("\\s+").length > 1) {
            return firstLine;
        }
        return reader.readLine();
    }
}
