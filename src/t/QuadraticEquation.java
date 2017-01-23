package t;

public class QuadraticEquation {

    final float a, b, c;
    private Result r;
    
    public QuadraticEquation(float a, float b, float c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
    
    public Result solve() {
        if (r == null) {
            r = new Result();
            
            if (a == 0) {
                if (b == 0) {
                    r.type = c == 0 ? Result.TYPE_ANY : Result.TYPE_INVALID;
                    return r;
                }
                
                r.type = Result.TYPE_SIGLE;
                r.x1 = r.x2 = -c / b;
                return r;
            }
            
            final float delta = b * b - a * c * 4;
            r.type = delta > 0 ? Result.TYPE_DOUBLE : delta == 0 ? Result.TYPE_SIGLE : Result.TYPE_NONE;
            
            if (r.type == Result.TYPE_NONE) {
                return r;
            }
            
            final float sqrtDelta = (float) Math.sqrt(delta);
            
            r.x1 = (-b + sqrtDelta) / (2 * a);
            r.x2 = (-b - sqrtDelta) / (2 * a);
        }
        
        return r;
    }
    
    public static class Result {
        public static final int TYPE_INVALID = -1;
        public static final int TYPE_NONE = 0;
        public static final int TYPE_SIGLE = 1;
        public static final int TYPE_DOUBLE = 2;
        public static final int TYPE_ANY = 3;
        
        public int type;
        public float x1;
        public float x2;
        
        public boolean isValid() {
            return type != TYPE_NONE;
        }
        
        @Override
        public String toString() {
            switch (type) {
                case TYPE_INVALID:
                    return "Invalid";
                case TYPE_NONE:
                    return "None";
                case TYPE_SIGLE:
                    return "Single: " + x1;
                case TYPE_DOUBLE:
                    return "Double: " + x1 + ", " + x2;
                case TYPE_ANY:
                    return "Any";
            }
            return super.toString();
        }
    }
}
