package util;


import geometry.Vertex;

import static geometry.Vertex.vertex;

public class Bezier {

    private Bezier() {
    }

    public static Vertex threePointBezier(BezierPoints bezierPoints, float t) {
        float oneMinusT = 1 - t;
        float returnPointX = bezierPoints.initialVertex.getX() * oneMinusT * oneMinusT +
                bezierPoints.controlVertex.getX() * 2 * oneMinusT * t +
                bezierPoints.finalVertex.getX() * t * t;
        float returnPointZ = bezierPoints.initialVertex.getZ() * oneMinusT * oneMinusT +
                bezierPoints.controlVertex.getZ() * 2 * oneMinusT * t +
                bezierPoints.finalVertex.getZ() * t * t;

        return vertex(returnPointX, bezierPoints.initialVertex.getY(), returnPointZ);
    }

    public static class BezierPoints {
        public final Vertex initialVertex;
        public final Vertex controlVertex;
        public final Vertex finalVertex;

        public BezierPoints(Vertex initialVertex, Vertex controlVertex, Vertex finalVertex) {
            this.initialVertex = initialVertex;
            this.controlVertex = controlVertex;
            this.finalVertex = finalVertex;
        }
    }

}
