package entity.enemy;

import geometry.Vertex;
import util.Bezier;

import java.util.List;

import static geometry.Vertex.vertex;
import static util.Bezier.threePointBezier;

public class EnemyTrajectory {

    private List<Bezier.BezierPoints> bezierPoints;
    private int index;
    private float bezierT;

    public EnemyTrajectory(int trajectoryId) {
        if (trajectoryId == 0) {
            bezierPoints = List.of(
                    new Bezier.BezierPoints(
                            vertex(-20, 20, -20),
                            vertex(-30, 20, 0),
                            vertex(-50, 20, 50)),
                    new Bezier.BezierPoints(
                            vertex(-50, 20, 50),
                            vertex(0, 20, 0),
                            vertex(20, 20, 20)),
                    new Bezier.BezierPoints(
                            vertex(20, 20, 20),
                            vertex(0, 20, 0),
                            vertex(20, 20, -20)),
                    new Bezier.BezierPoints(
                            vertex(20, 20, -20),
                            vertex(0, 20, 0),
                            vertex(-20, 20, -20))
            );
        }
        if (trajectoryId == 1) {
            bezierPoints = List.of(
                    new Bezier.BezierPoints(
                            vertex(-45, 20, 30),
                            vertex(10, 20, 30),
                            vertex(40, 20, 40)),
                    new Bezier.BezierPoints(
                            vertex(40, 20, 40),
                            vertex(70, 20, 50),
                            vertex(-40, 20, -30)),
                    new Bezier.BezierPoints(
                            vertex(-40, 20, -30),
                            vertex(20, 20, 20),
                            vertex(-45, 20, 30))

            );
        }
        if (trajectoryId == 2) {
            bezierPoints = List.of(
                    new Bezier.BezierPoints(
                            vertex(35, 20, 44),
                            vertex(50, 20, 0),
                            vertex(0, 20, 22)),
                    new Bezier.BezierPoints(
                            vertex(0, 20, 22),
                            vertex(-10, 20, -25),
                            vertex(-25, 20, -45)),
                    new Bezier.BezierPoints(
                            vertex(-25, 20, -45),
                            vertex(10, 20, 10),
                            vertex(0, 20, 12)),
                    new Bezier.BezierPoints(
                            vertex(0, 20, 12),
                            vertex(30, 20, 26),
                            vertex(35, 20, 44))
            );
        }
        index = 0;
        bezierT = 0.0f;
    }

    public Vertex nextPosition() {
        if (bezierT > 1.0f) {
            bezierT = 0;
            index = (index + 1) % bezierPoints.size();
        } else {
            bezierT += .003f;
        }
        return threePointBezier(bezierPoints.get(index), bezierT);
    }

}
