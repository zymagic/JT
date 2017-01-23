package t;

public class PointF {

    public float x, y;
    
    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
