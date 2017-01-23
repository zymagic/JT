package t.math;

public class Line3D {
    Vector3D p;
    Vector3D n;

    public Line3D(Vector3D point, Vector3D dir) {
        if (dir == null || dir.length() == 0) {
            throw new IllegalArgumentException("vector 0 is invalid for a line");
        }
        this.p = point;
        this.n = dir;
    }
}
