package t.math;

public class Vector3D {
    float x, y, z;

    public Vector3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public float multiply(Vector3D r) {
        return r.x * x + r.y * y + r.z * z;
    }

    public void rotate(float rx, float ry, float rz) {
        if (x != 0 || y != 0) {
            final float sin = (float) Math.sin(Math.toRadians(rz));
            final float cos = (float) Math.cos(Math.toRadians(rz));
            float nx = x * cos - y * sin;
            float ny = y * cos + x * sin;
            x = nx;
            y = ny;
        }

        if (y != 0 || z != 0) {
            final float sin = (float) Math.sin(Math.toRadians(rx));
            final float cos = (float) Math.cos(Math.toRadians(rx));
            float ny = y * cos - z * sin;
            float nz = z * cos + y * sin;
            y = ny;
            z = nz;
        }

        if (z != 0 || x != 0) {
            final float sin = (float) Math.sin(Math.toRadians(ry));
            final float cos = (float) Math.cos(Math.toRadians(ry));
            float nz = z * cos - x * sin;
            float nx = x * cos + z * sin;
            z = nz;
            x = nx;
        }
    }

    @Override
    public String toString() {
        return "Vector(" + x + ", " + y + "," + z + ")";
    }

    public void rotateByLine(float rx, float ry, float rz, float r) {
        float lr = (float) Math.sqrt(rx * rx + ry * ry + rz * rz);
        if (lr == 0) {
            return;
        }

        float ll = (float) Math.sqrt(x * x + y * y + z * z );
        if (ll == 0) {
            return;
        }

        float ix = rx / lr;
        float iy = ry / lr;
        float iz = rz / lr;

        float sinw = iz;
        float cosw = (float) Math.sqrt(1 - iz * iz);
        if (cosw == 0) {
            double radian = Math.toRadians(r * Math.signum(rz));
            float cosr = (float) Math.cos(radian);
            float sinr = (float) Math.sin(radian);
            float nx = x * cosr - y * sinr;
            float ny = y * cosr + x * sinr;
            x = nx;
            y = ny;
            return;
        }

        float sinj = iy / cosw;
        float cosj = ix / cosw;

        float x1 = x * cosj + y * sinj;
        float y1 = y * cosj - x * sinj;

        float z1 = z * cosw - x1 * sinw;
        x1 = x1 * cosw + z * sinw;

        float cosr = (float) Math.cos(Math.toRadians(r));
        float sinr = (float) Math.sin(Math.toRadians(r));

        float y2 = y1 * cosr - z1 * sinr;
        float z2 = z1 * cosr + y1 * sinr;
        float x2 = x1;

        float z3 = z2 * cosw + x2 * sinw;
        float x3 = x2 * cosw - z2 * sinw;
        float y3 = y2;

        float x4 = x3 * cosj - y3* sinj;
        float y4 = y3 * cosj + x3 * sinj;

        x = x4;
        y = y4;
        z = z3;

    }
}
