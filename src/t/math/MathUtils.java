package t.math;

public class MathUtils {

    public static Vector3D getLSIntersectionPoint(Line3D line, Surface3D surface) {
        float cross = line.n.multiply(surface.n);
        if (cross == 0) {
            // parallel case
            return null;
        }
        float d = -surface.value(line.p.x, line.p.y, line.p.z) / surface.n.length();
        float dr = d / (cross / surface.n.length() / line.n.length());
        float dl = line.n.length();
        return new Vector3D(line.p.x + line.n.x / dl * dr, line.p.y + line.n.y / dl * dr, line.p.z + line.n.z / dl * dr);
    }

}
