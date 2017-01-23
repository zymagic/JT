package t;

/**
 * 直线
 * */
public class Line {

 // ax + by + c = 0
    final float a, b;
    float c;
    
    /**
     * 点斜式
     *  y = ax + b
     */
    public Line(float a, float b) {
        this(a, -1, b);
    }
    
    /**
     * 方程
     * ax + by + c = 0
     * */
    public Line(float a, float b, float c) {
        this.a = a;
        this.b = b;
        this.c = c;
        System.out.println(a + ", " + b + ", " + c);
    }
    
    /**
     * 两点式
     * (x - x1)(y2 - y1) = (y - y1)(x2 - x1)
     * */
    public Line(float x1, float y1, float x2, float y2) {
        this(y2 - y1, x1 - x2, x2 * y1 - x1 * y2);
    }
    
    public float value(float x, float y) {
        return a * x + b * y + c;
    }
    
    public boolean contains(float x, float y) {
        return value(x, y) == 0f;
    }

    public void offset(float dx, float dy) {
        c -= a * dx + b * dy;
    }
    
    /**
     * 重合
     * */
    public boolean same(Line l) {
        return a * l.b == b * l.a && a * l.c == c * l.a && b * l.c == c * l.b;
    }
    
    /**
     * 相交
     * */
    public boolean cross(Line l) {
        return a * l.b != b * l.a;
    }
    
    /**
     * 平行
     * */
    public boolean parallel(Line l) {
        return a * l.b == b * l.a && a * l.c != c * l.a && b * l.c != c * l.b;
    }
    
    /**
     * 垂直
     * */
    public boolean perpendicularTo(Line l) {
        return a * l.a + b * l.b == 0f;
    }
}
