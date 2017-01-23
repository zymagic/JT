package t;

/**
 * 二次贝塞尔曲线
 * */
public class Bezier {

    PointF a = new PointF();
    PointF b = new PointF();
    PointF c = new PointF();
    
    public Bezier(float x1, float y1, float x2, float y2, float x3, float y3) {
        setStartPoint(x1, y1);
        setControlPoint(x2, y2);
        setEndPoint(x3, y3);
    }
    
    public void setStartPoint(float x, float y) {
        a.set(x, y);
    }
    
    public void setEndPoint(float x, float y) {
        b.set(x, y);
    }
    
    public void setControlPoint(float x, float y) {
        c.set(x, y);
    }
    
    public void getPoint(float t, PointF out) {
        if (out == null) {
            return;
        }
        
        final float x = bezier(t, a.x, c.x, b.x);
        final float y = bezier(t, a.y, c.y, b.y);
        out.set(x, y);
    }
    
    private static float bezier(float t, float a, float c, float b) {
        return (1 - t) * (1 - t) * a + 2 * t * (1 - t) * c + t * t * b;
    }
    
    public boolean contains(float x, float y) {
        QuadraticEquation.Result r1 = getEquationResult(a.x, c.x, b.x, x);
        QuadraticEquation.Result r2 = getEquationResult(a.y, c.y, b.y, y);
        System.out.println(r1);
        System.out.println(r2);
        if (r1.isValid() && r2.isValid()) {
            if (r1.x1 == r2.x1 || r1.x1 == r2.x2) {
                if (isValidResult(r1.x1)) {
                    return true;
                }
            } 
            
            if (r1.x2 == r2.x1 || r1.x2 == r2.x2) {
                if (isValidResult(r1.x2)) {
                    return true;
                }
            }

            return false;
        }
        return false;
    }
    
    private static QuadraticEquation.Result getEquationResult(float a, float c, float b, float value) {
        return new QuadraticEquation(a - 2 * c + b, -2 * a + 2 * c, a - value).solve();
    }
    
    private static boolean isValidResult(float value) {
        return value >= 0 && value <= 1;
    }
}
