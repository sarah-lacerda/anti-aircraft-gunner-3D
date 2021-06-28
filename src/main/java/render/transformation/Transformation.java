package render.transformation;

import geometry.Vertex;

public interface Transformation {
    void apply();

    static Scale scale(Vertex atPosition, float factorX, float factorY, float factorZ) {
        return new Scale(atPosition, factorX, factorY, factorZ);
    }

    static Translate translate(Vertex toPosition) {
        return new Translate(toPosition);
    }

    static Rotate rotate(Vertex around, float angle) {
        return new Rotate(around, angle);
    }
}
