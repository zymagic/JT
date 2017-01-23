
package t;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;

import javax.imageio.ImageIO;

public class Test {

    private static final String TTT = "赵钱孙李周吴郑王";

    private static float gauss(int x, int y, float sigma) {
        return (float) (Math.exp(-(x*x + y * y) / (2f * sigma * sigma)) / (2f * Math.PI * sigma * sigma));
    }

    public enum FileType {
        Image    ("png", "jpg", "gif", "bmp"),
        Video    ("avi", "rmvb", "rm", "mpeg4", "mp4", "wmv", "3gp"),
        Audio    ("wav", "mp3", "ape", "flac", "wma"),
        Document ("txt", "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "xml", "htm", "html"),
        Zip      ("zip", "7z", "gzip", "rar"),
        Unknown  ();

        Pattern p;

        FileType(String ... ext) {
            if (ext == null || ext.length == 0) {
                p = Pattern.compile("\\w+.*");
            } else {
                StringBuilder sb = new StringBuilder("^\\w+.*\\.(?:");
                boolean first = true;
                for (String s : ext) {
                    if (first) {
                        first = false;
                    } else {
                        sb.append("|");
                    }
                    sb.append(s);
                }
                sb.append(")$");
                p = Pattern.compile(sb.toString());
            }
            System.out.println(p.pattern());
        }

        public boolean match(String path) {
            if (path == null) {
                return false;
            }
            return p.matcher(path).matches();
        }

        public boolean match(File f) {
            if (f == null) {
                return false;
            }
            return p.matcher(f.getName()).matches();
        }
    }

    static final int K = 1024;
    static final int M = K * K;
    static final int G = K * M;

    public static class MyClass {

        private void mym() {
            System.out.println("XXXX");
        }
    }

    public static void main(String[] args) throws IOException {
//        Gaussian.gaussian(5);

//        FileOutputStream fos = new FileOutputStream("C:\\Users\\zhangyang\\Desktop\\ascii");
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < Character.MAX_VALUE; i++) {
//           sb.append(i + "char=");
//           sb.append((char)i);
//           sb.append("end");
//           sb.append("\n");
//        }
//        fos.write(sb.toString().getBytes());

        getC(1.05);

//        BlurEx b = new BlurEx("C:\\Users\\zhangyang\\Desktop\\gauss.png", "C:\\Users\\zhangyang\\Desktop\\gauss");
//        int n = 30;
//        for (int i = 0; i < n; i++) {
//            int radius = (i + 1);
//            System.out.println("start blur with radius radius " + radius);
////            b.blur1(radius);
////            b.blur2(radius);
////            b.blur3(radius);
//////            b.blur4(radius);
////            b.blur5(radius);
//            b.blur6(radius);
//            b.blur7(radius);
//        }

//        for (int i = 0; i < 16; i++) {
//            System.out.println((int) (2 + Math.ceil(Math.abs((i - 6) / 3f)) * Math.signum(i - 6)));
//        }

//        Line3D line = new Line3D(new Vector3D(1, 1, 1), new Vector3D(1, 1, 0));
//        Surface3D surface = new Surface3D(new Vector3D(0, 0, 1), new Vector3D(0, 0, 1));
//        System.out.println(MathUtils.getLSIntersectionPoint(line, surface));
//        new PackageTools("C:\\Users\\zhangyang\\Desktop\\packages.txt", "C:\\Users\\zhangyang\\Desktop\\3D主题\\space\\launcher").exe();

//        Vector3D v = new Vector3D(1, 0, 0);
//        v.rotateByLine(-1, 0, 1, 90);
//        System.out.println(v);

//        System.out.println("  \n  te\nst\r\n  ".trim().replaceAll("[\\r\\n]", ""));
//
//        Calendar c = Calendar.getInstance();
//        c.setTimeZone(TimeZone.getTimeZone("GMT+0"));
//        System.out.println(c.getTimeInMillis());
//        System.out.println(System.currentTimeMillis());
//        System.out.println(new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss").format(new Date(1459301400000l)));
//
//        System.out.println(sso2("JTO", 6));
//
//        System.out.println(String.format("%02d", 123));

//        File dir = new File("C:\\Users\\zhangyang\\Desktop");
//        File wp = new File("C:\\Users\\zhangyang\\Desktop\\dialog_default_launcher_bg.jpg");
//        File weather = new File("C:\\Users\\zhangyang\\Desktop\\clock_preview.png");
//        File[] icons = new File("C:\\Users\\zhangyang\\Desktop\\launcher").listFiles();
//        try {
//            new PreviewGenerator(wp, icons, weather, dir).run();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        System.out.println("done");

        File file = new File("C:\\Users\\j-zhangyang5\\Desktop\\vungle-sdk-bundled-20170111.jar");
        CRC32 checksum = new CRC32();
        checksum(file, checksum);
        System.out.println(Long.toHexString(checksum.getValue()));
    }

    private static Checksum checksum(File file, Checksum checksum) throws IOException {
        if (file.isDirectory()) {
            throw new IllegalArgumentException("Checksums can't be computed on directories");
        }
        InputStream in = null;
        try {
            in = new CheckedInputStream(new FileInputStream(file), checksum);
            byte[] buffer = new byte[1024 * 4];
            int len = -1;
            while (-1 != (len = in.read(buffer))) {
                // ignore
            }
        } finally {
            if (null != in) in.close();
        }
        return checksum;
    }

    public static void shellSort(int[] a) {
        int i, j, k, t;
        int n = a.length;
        k = n / 2;
        while (k > 0) {
            for (i = k; i < n; i++) {
                t = a[i];
                j = i - k;
                while (j >= 0 && t < a[j]) {
                    a[j + k] = a[j];
                    j = j - k;
                }
                a[j + k] = t;
            }
            k /= 2;
        }
    }

    public static int getByteLength(String src) {
        if (src == null) {
            return 0;
        }

        final int strLen = src.length();
        int len = strLen;
        for (int i = 0; i < strLen; i++) {
            char c = src.charAt(i);
            c >>>= 8;
            while ((c & 0xff) > 0) {
                len++;
                c >>>= 8;
            }
        }
        return len;
    }

    static abstract class Tester {
        static int sid;
        private String name;

        public Tester(String name) {
            if (name == null) {
                this.name = "Tester" + sid++;
            } else {
                this.name = name;
            }
        }

        public void runTest() throws IOException {
            long start = System.nanoTime();
            test();
            long end = System.nanoTime();

            System.out.format( "%s cost %.3f\n", name, (end - start) / 1.0e9);
        }

        public abstract void test() throws IOException;
    }

    static boolean equals(String p, String h) {
        int pl = p.length();
        int ps = p.startsWith("/") ? 1 : 0;
        int pe = p.endsWith("/") ? 1 : 0;
        int hl = h.length();
        int hs = h.startsWith("/") ? 1 : 0;
        int he = h.endsWith("/") ? 1 : 0;

        if (pl - ps - pe != hl - hs - he) {
            return true;
        }

        int n = pl - ps - pe;
        while (n-- > 0) {
            if (p.charAt(ps + n) != h.charAt(hs + n)) {
                return true;
            }
        }

        return false;
    }

    static int[] compress(int[] colors) {
		if (colors == null) {
			return null;
		}

		if (colors.length == 1) {
			final int color = colors[0];
			return new int[] {color};
		}

		int[] out = new int[(int) (colors.length * 1.125f + 0.5f)];

		int di = 0; // data index
		int ci = out.length - 1; // count index
		int cbi = -4; // count bit index

		int c = 0; // count
		int cmp = -1; // compare color

		for (int color : colors) {
			if (color == cmp && ++c < 15) {
				out[ci] &= ~(0xf << cbi);
				out[ci] |= (c + 1) << cbi;
			} else {

				if (cbi == 28) {
					ci--;
					cbi = 0;
				} else {
					cbi += 4;
				}

				out[di] = color;
				out[ci] &= ~(0xf << cbi);
				out[ci] |= 1 << cbi;
				di++;

				cmp = color;
				c = 0;
			}
		}

		ci = cbi == 0 ? ci + 1 : ci;

		if (di < ci) {
			int[] old = out;
			out = new int[di + old.length - ci];
			System.arraycopy(old, 0, out, 0, di);
			System.arraycopy(old, ci, out, di, old.length - ci);
		}

		return out;
	}

    static int[] decompress(int[] src, int len) {
    	if (src == null) {
    		return null;
    	}

    	if (src.length == 1) {
    		return new int[] {src[0]};
    	}

    	int[] colors = new int[len];
    	int index = 0;
    	int hitIndex = src.length - 1;
    	int countIndex = 0;
    	int hitCount = 0;
    	int ci = 0;
    	while (index < hitIndex) {
    		hitCount = (src[hitIndex] >>> countIndex) & 0x0f;

    		if (countIndex == 28) {
    			hitIndex--;
    			countIndex = 0;
    		} else {
    			countIndex += 4;
    		}

    		int color = src[index];

    		while (hitCount-- > 0) {
    			colors[ci++] = color;
    		}

    		index ++;
    	}

    	return colors;
    }

    private static float rd(int a, int b, int c, int d) {
        return (float) ((b * c - a * d) / Math.sqrt(a * a + b * b));
    }

    private static float angle(float a, float b, float c, float d) {
        return (float) Math.acos((a * c + b * d) / Math.sqrt(a * a + b * b) / Math.sqrt(c * c + d * d))
                / (b * (c - a) - a * (d - b) > 0 ? -1 : 1);
    }


    //    public static void haar(int w, int h, float[][] colors) {
    //        for (int i = 0; i < w; i++) {
    //            for (int j = 0; j < w / 2; j++) {
    //                float v1 = colors[i][2 * j];
    //                float v2 = colors[i][2 * j + 1];
    //                tempData[j] = (v1 + v2) / 2;
    //                tempData[j + 4] = (v1 - v2) / 2;
    //            }
    //
    //            for (int j = 0; j < w; j++) {
    //                colors[i][j] = tempData[j];
    //            }
    //        }
    //
    //        for (int i = 0; i < h; i++) {
    //            for (int j = 0; j < h / 2; j++) {
    //                float v1 = colors[2 * j][i];
    //                float v2 = colors[2 * j + 1][i];
    //                tempData[j] = (v1 + v2) / 2;
    //                tempData[j + h / 2] = (v1 - v2) / 2;
    //            }
    //
    //            for (int j = 0; j < up; j++) {
    //                colors[j][i] = tempData[j];
    //            }
    //        }
    //    }

    public static float distance(Line l1, Line l2) {
        final float r1 = (float) Math.sqrt(l1.a * l1.a + l1.b * l1.b);
        final float r2 = (float) Math.sqrt(l2.a * l2.a + l2.b * l2.b);
        return Math.abs(l1.c * r2 - l2.c * r1) / (r1 * r2);
    }

    public static class Choose {

        private final int total;

        private Random random;

        private int[] candidates;

        private int choosed = 0;

        public Choose(int total) {
            this.total = total;

            candidates = new int[total];
            for (int i = 0; i < total; i++) {
                candidates[i] = i;
            }

            random = new Random();
        }

        public int choose() {
            int index = random.nextInt(total - choosed);
            int c = candidates[index];
            candidates[index] = candidates[total - choosed - 1];
            choosed++;
            return c;
        }
    }

    private static void getC(double t) {
        double b = 2 * t + 2 * t * Math.sqrt(1 - 1 / t);
        double a = 1 - b;
        System.out.println("a=" + a + ", b=" + b + ", t=" + (-b*b/a/4) + ", m=" + (-b / 2 / a));
    }

    private static void getO(float a) {
        System.out.println("m = " + (4 * a * a * a / 27f / (a + 1) / (a + 1)));
    }

    public static class AAA
    {
        int aaa = 100;
        public AAA(int arg)
        {
            Init(arg);
        }
        protected void Init(int arg)
        {
            System.out.println("AAA-1 " +arg);
        }
    }
    public static class BBB extends AAA
    {
        int bbb = 200;

        AAA a = new AAA(50);

        public BBB(int arg)
        {
            super(arg);
            System.out.println("BBB-1 "+bbb);
        }
        protected void Init(int arg)
        {
            super.Init(arg);
            System.out.println("BBB-2 "+bbb);
        }
    }

    public static String so(String in) {
        if (in == null) {
            return null;
        }
        if (in.length() == 0) {
            return in;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);
            if (c < 30) {
                c = (char) (29 - c);
            } else if (c >= 30 && c <= 39) {
                c = (char) (48 + 57 - c);
            } else if (c > 39 && c < 65) {
                c = (char) (64 - c + 40);
            } else if (c >= 65 && c <= 90) {
                c = (char) (122 - c + 65);
            } else if (c > 90 && c < 97) {
                c = (char) (96 - c + 90);
            } else if (c >= 97 && c <= 122) {
                c = (char) (90 - c + 97);
            } else if (c <= 127) {
                c = (char) (127 - c + 123);
            }
            sb.append(c);
        }
        return sb.toString();
    }

    public static String so2(String in) {
        if (in == null) {
            return null;
        }
        if (in.length() == 0) {
            return in;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);
            char origin = c;
            if (c <= 127) {
                c = (char) (((c / 32 + 2) % 4 + 1) * 32 - c + c / 32 * 32);
            }
            System.out.println("change from " + origin + "(" + (int) origin + ") to " + c + "("+ (int)c +")");
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * @param key integer from 1 to 7
     * */
    public static String sso2(String in, int key) {
        System.out.println(key);
        if (in == null) {
            return null;
        }
        if (in.length() == 0) {
            return in;
        }
        StringBuilder sb = new StringBuilder();
        int piece = (int) Math.pow(2, key);
        int part = 128 / piece;
        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);
            char origin = c;
            if (c < 128) {
                c = (char) (((c / piece + part / 2) % part + 1) * piece - 1 - c + c / piece * piece);
            }
            System.out.println("change from " + origin + "(" + (int) origin + ") to " + c + "("+ (int)c +")");
            sb.append(c);
        }
        return sb.toString();
    }


    public static class BlurEx {
        String file;
        String out;
        public BlurEx(String file, String output) {
            this.file = file;
            this.out = output;
        }

        public void blur1(int radius) throws IOException {
            BufferedImage image = ImageIO.read(new File(file));
            long start = System.currentTimeMillis();
            int w = image.getWidth();
            int h = image.getHeight();
            int[] c = new int[w * h];
            image.getRGB(0, 0, w, h, c, 0, w);
            long time = Blur.blur1(c, w, h, radius);
            image.setRGB(0, 0, w, h, c, 0, w);
            long end = System.currentTimeMillis();
            ImageIO.write(image, "PNG", new File(out, "gaussian1-" + radius + ".png"));
            System.out.println("blur1 cost " + (end - start) + ", core " + time);
        }

        public void blur2(int radius) throws IOException  {
            BufferedImage image = ImageIO.read(new File(file));
            long start = System.currentTimeMillis();
            int w = image.getWidth();
            int h = image.getHeight();
            int[] c = new int[w * h];
            image.getRGB(0, 0, w, h, c, 0, w);
            long time = Blur.blur2(c, w, h, radius);
            image.setRGB(0, 0, w, h, c, 0, w);
            long end = System.currentTimeMillis();
            ImageIO.write(image, "PNG", new File(out, "gaussian2-" + radius + ".png"));
            System.out.println("blur2 cost " + (end - start) + ", core " + time);
        }

        public void blur3(int radius) throws IOException  {
            BufferedImage image = ImageIO.read(new File(file));
            long start = System.currentTimeMillis();
            int w = image.getWidth();
            int h = image.getHeight();
            int[] c = new int[w * h];
            image.getRGB(0, 0, w, h, c, 0, w);
            long time = Blur.blur(c, w, h, radius);
            image.setRGB(0, 0, w, h, c, 0, w);
            long end = System.currentTimeMillis();
            ImageIO.write(image, "PNG", new File(out, "gaussian3-" + radius + ".png"));
            System.out.println("blur3 cost " + (end - start) + ", core " + time);
        }

        public void blur4(int radius) throws IOException  {
            BufferedImage image = ImageIO.read(new File(file));
            long start = System.currentTimeMillis();
            int w = image.getWidth();
            int h = image.getHeight();
            int[] c = new int[w * h];
            image.getRGB(0, 0, w, h, c, 0, w);
            long time = Blur.blur3(c, w, h, radius);
            image.setRGB(0, 0, w, h, c, 0, w);
            long end = System.currentTimeMillis();
            ImageIO.write(image, "PNG", new File(out, "gaussian4-" + radius + ".png"));
            System.out.println("blur4 cost " + (end - start) + ", core " + time);
        }

        public void blur5(int radius) throws IOException  {
            BufferedImage image = ImageIO.read(new File(file));
            long start = System.currentTimeMillis();
            int w = image.getWidth();
            int h = image.getHeight();
            int[] c = new int[w * h];
            image.getRGB(0, 0, w, h, c, 0, w);
            long time = Blur.blur4(c, w, h, radius);
            image.setRGB(0, 0, w, h, c, 0, w);
            long end = System.currentTimeMillis();
            ImageIO.write(image, "PNG", new File(out, "gaussian5-" + radius + ".png"));
            System.out.println("blur5 cost " + (end - start) + ", core " + time);
        }

        public void blur6(int radius) throws IOException  {
            BufferedImage image = ImageIO.read(new File(file));
            long start = System.currentTimeMillis();
            int w = image.getWidth();
            int h = image.getHeight();
            int[] c = new int[w * h];
            image.getRGB(0, 0, w, h, c, 0, w);
            long time = Blur.blur5(c, w, h, radius);
            image.setRGB(0, 0, w, h, c, 0, w);
            long end = System.currentTimeMillis();
            ImageIO.write(image, "PNG", new File(out, "gaussian6-" + radius + ".png"));
            System.out.println("blur6 cost " + (end - start) + ", core " + time);
        }

        public void blur7(int radius) throws IOException  {
            BufferedImage image = ImageIO.read(new File(file));
            long start = System.currentTimeMillis();
            int w = image.getWidth();
            int h = image.getHeight();
            int[] c = new int[w * h];
            image.getRGB(0, 0, w, h, c, 0, w);
            long time = Blur.blur6(c, w, h, radius);
            image.setRGB(0, 0, w, h, c, 0, w);
            long end = System.currentTimeMillis();
            ImageIO.write(image, "PNG", new File(out, "gaussian7-" + radius + ".png"));
            System.out.println("blur7 cost " + (end - start) + ", core " + time);
        }
    }
}