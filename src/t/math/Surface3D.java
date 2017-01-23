package t.math;

public class Surface3D {

    Vector3D p;
    Vector3D n;
    private float c;

    public Surface3D(Vector3D point, Vector3D dir) {
        if (dir == null || dir.length() == 0) {
            throw new IllegalArgumentException("vector 0 is invalid for a surface");
        }
        this.p = point;
        this.n = dir;
        this.c = -(p.x * n.x + p.y * n.y + p.z * n.z);
    }

    public float value(float x, float y, float z) {
        return n.x * x + n.y * y + n.z * z + c;
    }
}
