
package t;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Gaussian {

    /**
     * 简单高斯模糊算法
     *
     * @param args
     * @throws IOException [参数说明]
     * @return void [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static void gaussian(int r) throws IOException {
        BufferedImage img = ImageIO.read(new File("c:\\Users\\zhangyang\\Desktop\\device.png"));
//        System.out.println(img);
//        int height = img.getHeight();
//        int width = img.getWidth();
//        int[][] matrix = new int[3][3];
//        int[] values = new int[9];
//        for (int i = 0; i < width; i++) {
//            for (int j = 0; j < height; j++) {
//                readPixel(img, i, j, values);
//                fillMatrix(matrix, values);
//                img.setRGB(i, j, avgMatrix(matrix));
//            }
//        }
        blurBitmap(img, r);
        ImageIO.write(img, "jpeg", new File("c:\\Users\\zhangyang\\Desktop\\test.jpg"));// 保存在d盘为test.jpeg文件
    }

    private static void readPixel(BufferedImage img, int x, int y, int[] pixels) {
        int xStart = x - 1;
        int yStart = y - 1;
        int current = 0;
        for (int i = xStart; i < 3 + xStart; i++) {
            for (int j = yStart; j < 3 + yStart; j++) {
                int tx = i;
                if (tx < 0) {
                    tx = -tx;
                } else if (tx >= img.getWidth()) {
                    tx = x;
                }

                int ty = j;
                if (ty < 0) {
                    ty = -ty;
                } else if (ty >= img.getHeight()) {
                    ty = y;
                }
                pixels[current++] = img.getRGB(tx, ty);
            }
        }
    }

    private static void fillMatrix(int[][] matrix, int... values) {
        int filled = 0;
        for (int i = 0; i < matrix.length; i++) {
            int[] x = matrix[i];
            for (int j = 0; j < x.length; j++) {
                x[j] = values[filled++];
            }
        }
    }

    private static int avgMatrix(int[][] matrix) {
        int r = 0;
        int g = 0;
        int b = 0;
        for (int i = 0; i < matrix.length; i++) {
            int[] x = matrix[i];
            for (int j = 0; j < x.length; j++) {
                if (j == 1) {
                    continue;
                }
                Color c = new Color(x[j]);
                r += c.getRed();
                g += c.getGreen();
                b += c.getBlue();
            }
        }
        return new Color(r / 8, g / 8, b / 8).getRGB();
    }

    static void blurBitmap(BufferedImage bmp, int r) {
        long start = System.currentTimeMillis();
        int d = 2 * r + 1;
        int n = d * d;
        double[] g = new double[n];
        for (int i = 0; i < n; i++) {
            g[i] = gaussian2(i % d - r, i / d - r, r);
        }
        float total = 0;
        for (int i = 0; i < n; i++) {
            total += g[i];
        }
        for (int i = 0; i < n; i++) {
            g[i] /= total;
        }

        int[] c = new int[n];
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        int[] cc = new int[w * h];
        bmp.getRGB(0, 0, w, h, cc, 0, w);
        for (int i = r; i < w - r; i++) {
            for (int j = r; j < h - r; j++) {
                bmp.getRGB(i - r, j - r, d, d, c, 0, d);
                double as = 0, rs = 0, gs = 0, bs = 0;
                int index = 0;
                for (int ccc : c) {
                    double gi = g[index++];
                    as += ((ccc >>> 24) & 0xff) * gi;
                    rs += ((ccc >>> 16) & 0xff) * gi;
                    gs += ((ccc >>> 8) & 0xff) * gi;
                    bs += (ccc & 0xff) * gi;
                }
                cc[j * w + i] = new Color((int) rs, (int) gs, (int) bs, (int) as).getRGB();
            }
        }
        bmp.setRGB(0, 0, w, h, cc, 0, w);
        long end = System.currentTimeMillis();
        System.out.println("guass2 cost " + (end - start));
    }

    static double gaussian2(int x, int y, float sigma) {
        float ret = (float) (Math.exp(-(x * x + y * y) / (2 * sigma * sigma)) / (2 * sigma * sigma * Math.PI));
        return ret;
    }

    static float gaussian1(int x, float sigma) {
        float ret = (float) (Math.exp(-x * x / (2 * sigma * sigma)) / (Math.sqrt(2 * Math.PI) * sigma));
        return ret;
    }

    static void foldingBlur(BufferedImage bmp, int r) {
        long start = System.currentTimeMillis();
        int d = 2 * r + 1;
        int n = d;
        double[] g = new double[n];
        double total = 0;
        for (int i = 0; i < n; i++) {
            g[i] = gaussian1(i % d - r, r);
            total += g[i];
        }
        for (int i = 0; i < n; i++) {
            g[i] /= total;
        }
        int[] c = new int[n];
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        int[] cc = new int[w * h];
        bmp.getRGB(0, 0, w, h, cc, 0, w);
        for (int j = 0; j < h; j++) {
            for (int i = r; i < w - r; i++) {
                bmp.getRGB(i - r, j, d, 1, c, 0, d);
                double as = 0, rs = 0, gs = 0, bs = 0;
                for (int m = 0; m < d; m++) {
                    int ccc = c[m];
                    double gi = g[m];
                    as += ((ccc >>> 24) & 0xff) * gi;
                    rs += ((ccc >>> 16) & 0xff) * gi;
                    gs += ((ccc >>> 8) & 0xff) * gi;
                    bs += (ccc & 0xff) * gi;
                }
                cc[j * w + i] = new Color((int) rs, (int) gs, (int) bs, (int) as).getRGB();
            }
        }
        bmp.setRGB(0, 0, w, h, cc, 0, w);
        for (int j = r; j < h - r; j++) {
            for (int i = 0; i < w; i++) {
                bmp.getRGB(i, j - r, 1, d, c, 0, 1);
                double as = 0, rs = 0, gs = 0, bs = 0;
                for (int m = 0; m < n; m++) {
                    int ccc = c[m];
                    double gi = g[m];
                    as += ((ccc >>> 24) & 0xff) * gi;
                    rs += ((ccc >>> 16) & 0xff) * gi;
                    gs += ((ccc >>> 8) & 0xff) * gi;
                    bs += (ccc & 0xff) * gi;
                }
                cc[j * w + i] = new Color((int) rs, (int) gs, (int) bs, (int) as).getRGB();
            }
        }
        bmp.setRGB(0, 0, w, h, cc, 0, w);
        long end = System.currentTimeMillis();
        System.out.println("gauss1 cost " + (end - start));
    }
}
