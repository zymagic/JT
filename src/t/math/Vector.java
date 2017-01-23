package t.math;

public class Vector {

    private final int order;

    private final float[] cs;

    public Vector(int order) {
        this.order = order;
        cs = new float[order];
    }

    public int order() {
        return order;
    }

    public float length() {
        float sum = 0f;
        for (int i = 0; i < order; i++) {
            sum += cs[i] * cs[i];
        }
        return (float) Math.sqrt(sum);
    }

    public void rotate(float ... rs) {

    }
}
