
package t;

import static t.Gaussian.gaussian2;
import static t.Gaussian.gaussian1;

public class Blur {

    public static long blur(int[] pix, int w, int h, int radius) {
        long start = System.currentTimeMillis();
        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }
        return System.currentTimeMillis() - start;
    }

    /**
     * Gaussian blur normal
     *
     * @param pixels the image colors array
     * @param w the with of the image
     * @param h the height of the image
     * @param r the blur radius
     *
     * */
    public static long blur1(int[] pixels, int w, int h, int r) {
        long start = System.currentTimeMillis();
        int d = 2 * r + 1;
        int n = d * d;
        float sigma = r * r;
        double[] g = new double[n];
        double total = 0;
        for (int i = 0; i < n; i++) {
            g[i] = gaussian2(i % d - r, i / d - r, sigma);
            total += g[i];
        }
        for (int i = 0; i < n; i++) {
            g[i] /= total;
        }

        int wh = w * h;
        int[] c = new int[wh];
        System.arraycopy(pixels, 0, c, 0, pixels.length);
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                double as = 0, ar = 0, ag = 0, ab = 0;
                for (int m = 0; m < n; m++) {
                    int cc = c[((j - r + m / d + h) % h) * w + (i - r + m %d + w) % w];
                    double gm = g[m];
                    as += (cc >>> 24) * gm;
                    ar += ((cc >> 16) & 0xFF) * gm;
                    ag += ((cc >> 8) & 0xFF) * gm;
                    ab += (cc & 0xFF) * gm;
                }
                pixels[j * w + i] = argb((int) as, (int) ar, (int) ag, (int) ab);
            }
        }
        return System.currentTimeMillis() - start;
    }

    /**
     * Gaussian blur improved
     *
     * @param pixels the image colors array
     * @param w the with of the image
     * @param h the height of the image
     * @param r the blur radius
     *
     * */
    public static long blur2(int[] pixels, int w, int h, int r) {
        long start = System.currentTimeMillis();
        int n = 2 * r + 1;
        float[] g = new float[n];
        float sigma = r / 2f;
        float total = 0;
        for (int i = 0; i < n; i++) {
            g[i] = gaussian1(i - r, sigma);
            total += g[i];
        }
        for (int i = 0; i < n; i++) {
            g[i] /= total;
        }

        int wh = w * h;
        int[] c = new int[wh];
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                float as = 0, ar = 0, ag = 0, ab = 0;
                for (int m = 0; m < n; m++) {
                    int cc = pixels[j * w + (i - r + m + w) % w];
                    float gm = g[m];
                    as += (cc >>> 24) * gm;
                    ar += ((cc >> 16) & 0xFF) * gm;
                    ag += ((cc >> 8) & 0xFF) * gm;
                    ab += (cc & 0xFF) * gm;
                }
                c[j * w + i] = argb((int) as, (int) ar, (int) ag, (int) ab);
            }
        }
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                float as = 0, ar = 0, ag = 0, ab = 0;
                for (int m = 0; m < n; m++) {
                    int cc = c[((j - r + m + h) % h) * w + i];
                    float gm = g[m];
                    as += (cc >>> 24) * gm;
                    ar += ((cc >> 16) & 0xFF) * gm;
                    ag += ((cc >> 8) & 0xFF) * gm;
                    ab += (cc & 0xFF) * gm;
                }
                pixels[j * w + i] = argb((int) as, (int) ar, (int) ag, (int) ab);
            }
        }
        return System.currentTimeMillis() - start;
    }

    /**
     * Stack blur
     *
     * @param pixels the image colors array
     * @param w the with of the image
     * @param h the height of the image
     * @param r the blur radius
     *
     * */
    public static long blur3(int[] pixels, int w, int h, int r) {
        long start = System.currentTimeMillis();
        int n = 2 * r + 1;
        double[] g = new double[n];
        double total = 0;
        for (int i = 0; i < n; i++) {
            g[i] = r - Math.abs(i - r) + 1;
            total += g[i];
        }
        for (int i = 0; i < n; i++) {
            g[i] /= total;
        }

        double eval = g[1] - g[0];

        double lsa, lsr, lsg, lsb, rsa, rsr, rsg, rsb, ssa, ssr, ssg, ssb, lssa, lssr, lssg, lssb;

        int wh = w * h;
        int[] c = new int[wh];
        for (int j = 0; j < h; j++) {
            lsa = lsr = lsg = lsb = rsa = rsr = rsg = rsb = ssa = ssr = ssg = ssb = lssa = lssg = lssb = lssr = 0;
            for (int m = 0; m < n; m++) {
                int cc = pixels[j * w + (m - r + w) % w];
                int alpha = (cc >>> 24);
                int red = ((cc >> 16) & 0xFF);
                int green = ((cc >> 8) & 0xFF);
                int blue = (cc & 0xFF);
                ssa += alpha;
                ssr += red;
                ssg += green;
                ssb += blue;
                double gm = g[m];
                if (m <= r) {
                    lssa += alpha;
                    lssr += red;
                    lssg += green;
                    lssb += blue;

                    lsa += alpha * gm;
                    lsr += red * gm;
                    lsg += green * gm;
                    lsb += blue * gm;
                } else {
                    rsa += alpha * gm;
                    rsr += red * gm;
                    rsg += green * gm;
                    rsb += blue * gm;
                }
            }
            c[j * w] = argb((int) (lsa + rsa), (int) (lsr + rsr), (int) (lsg + rsg), (int) (lsb + rsb));
            for (int i = 1; i < w; i++) {
                int cc = pixels[j * w + i];
                int ccp = pixels[j * w + (i - 1 - r + w) % w];
                int ccn = pixels[j * w + (i + r) % w];

                int cca = cc >>> 24;
                int ccr = (cc >> 16) & 0xFF;
                int ccg = (cc >> 8) & 0xFF;
                int ccb = cc & 0xFF;

                int ccpa = ccp >>> 24;
                int ccpr = (ccp >> 16) & 0xFF;
                int ccpg = (ccp >> 8) & 0xFF;
                int ccpb = ccp & 0xFF;

                int ccna = ccn >>> 24;
                int ccnr = (ccn >> 16) & 0xFF;
                int ccng = (ccn >> 8) & 0xFF;
                int ccnb = ccn & 0xFF;

                lsa = lsa - ccpa * g[0] - (lssa - ccpa) * eval + cca * g[r];
                rsa = rsa - cca * g[r + 1] + ccna * g[0] + (ssa - lssa - cca) * eval;
                ssa = ssa - ccpa + ccna;
                lssa = lssa - ccpa + cca;

                lsr = lsr - ccpr * g[0] - (lssr - ccpr) * eval + ccr * g[r];
                rsr = rsr - ccr * g[r + 1] + ccnr * g[0] + (ssr - lssr - ccr) * eval;
                ssr = ssr - ccpr + ccnr;
                lssr = lssr - ccpr + ccr;

                lsg = lsg - ccpg * g[0] - (lssg - ccpg) * eval + ccg * g[r];
                rsg = rsg - ccg * g[r + 1] + ccng * g[0] + (ssg - lssg - ccg) * eval;
                ssg = ssg - ccpg + ccng;
                lssg = lssg - ccpg + ccg;

                lsb = lsb - ccpb * g[0] - (lssb - ccpb) * eval + ccb * g[r];
                rsb = rsb - ccb * g[r + 1] + ccnb * g[0] + (ssb - lssb - ccb) * eval;
                ssb = ssb - ccpb + ccnb;
                lssb = lssb - ccpb + ccb;

                c[j * w + i] = argb((int) (lsa + rsa), (int) (lsr + rsr), (int) (lsg + rsg), (int) (lsb + rsb));
            }
        }
        for (int i = 0; i < w; i++) {
            lsa = lsr = lsg = lsb = rsa = rsr = rsg = rsb = ssa = ssr = ssg = ssb = lssa = lssg = lssb = lssr = 0;
            for (int m = 0; m < n; m++) {
                int cc = c[((m - r + h) % h) * w + i];
                int alpha = (cc >>> 24);
                int red = ((cc >> 16) & 0xFF);
                int green = ((cc >> 8) & 0xFF);
                int blue = (cc & 0xFF);
                ssa += alpha;
                ssr += red;
                ssg += green;
                ssb += blue;
                double gm = g[m];
                if (m <= r) {
                    lssa += alpha;
                    lssr += red;
                    lssg += green;
                    lssb += blue;

                    lsa += alpha * gm;
                    lsr += red * gm;
                    lsg += green * gm;
                    lsb += blue * gm;
                } else {
                    rsa += alpha * gm;
                    rsr += red * gm;
                    rsg += green * gm;
                    rsb += blue * gm;
                }
            }
            pixels[i] = argb((int) (lsa + rsa), (int) (lsr + rsr), (int) (lsg + rsg), (int) (lsb + rsb));
            for (int j = 1; j < h; j++) {
                int cc = c[j * w + i];
                int ccp = c[((j - r - 1 + h) % h) * w + i];
                int ccn = c[((j + r) % h) * w + i];

                int cca = cc >>> 24;
                int ccr = (cc >> 16) & 0xFF;
                int ccg = (cc >> 8) & 0xFF;
                int ccb = cc & 0xFF;

                int ccpa = ccp >>> 24;
                int ccpr = (ccp >> 16) & 0xFF;
                int ccpg = (ccp >> 8) & 0xFF;
                int ccpb = ccp & 0xFF;

                int ccna = ccn >>> 24;
                int ccnr = (ccn >> 16) & 0xFF;
                int ccng = (ccn >> 8) & 0xFF;
                int ccnb = ccn & 0xFF;

                lsa = lsa - ccpa * g[0] - (lssa - ccpa) * eval + cca * g[r];
                rsa = rsa - cca * g[r + 1] + ccna * g[0] + (ssa - lssa - cca) * eval;
                ssa = ssa - ccpa + ccna;
                lssa = lssa - ccpa + cca;

                lsr = lsr - ccpr * g[0] - (lssr - ccpr) * eval + ccr * g[r];
                rsr = rsr - ccr * g[r + 1] + ccnr * g[0] + (ssr - lssr - ccr) * eval;
                ssr = ssr - ccpr + ccnr;
                lssr = lssr - ccpr + ccr;

                lsg = lsg - ccpg * g[0] - (lssg - ccpg) * eval + ccg * g[r];
                rsg = rsg - ccg * g[r + 1] + ccng * g[0] + (ssg - lssg - ccg) * eval;
                ssg = ssg - ccpg + ccng;
                lssg = lssg - ccpg + ccg;

                lsb = lsb - ccpb * g[0] - (lssb - ccpb) * eval + ccb * g[r];
                rsb = rsb - ccb * g[r + 1] + ccnb * g[0] + (ssb - lssb - ccb) * eval;
                ssb = ssb - ccpb + ccnb;
                lssb = lssb - ccpb + ccb;

                pixels[j * w + i] = argb((int) (lsa + rsa), (int) (lsr + rsr), (int) (lsg + rsg), (int) (lsb + rsb));
            }
        }
        long end = System.currentTimeMillis();
        return end - start;
    }

    /**
     * Stack blur alter.
     *
     * @param pixels the image colors array
     * @param w the with of the image
     * @param h the height of the image
     * @param r the blur radius
     *
     * */
    public static long blur4(int[] pixels, int w, int h, int r) {
        long start = System.currentTimeMillis();
        int n = 2 * r + 1;
        int dv = (r + 1) * (r + 1);

        int lsa, lsr, lsg, lsb, rsa, rsr, rsg, rsb, ssa, ssr, ssg, ssb, lssa, lssr, lssg, lssb;

        int wh = w * h;
        int pw = (1 + r / w) * w;
        int ph = (1 + r / h) * h;
        int[] c = new int[wh];
        for (int j = 0; j < h; j++) {
            lsa = lsr = lsg = lsb = rsa = rsr = rsg = rsb = ssa = ssr = ssg = ssb = lssa = lssg = lssb = lssr = 0;
            for (int m = 0; m < n; m++) {
                int cc = pixels[j * w + (m - r + pw) % w];
                int alpha = (cc >>> 24);
                int red = ((cc >> 16) & 0xFF);
                int green = ((cc >> 8) & 0xFF);
                int blue = (cc & 0xFF);
                ssa += alpha;
                ssr += red;
                ssg += green;
                ssb += blue;
                int gm = r + 1 - Math.abs(m - r);
                if (m <= r) {
                    lssa += alpha;
                    lssr += red;
                    lssg += green;
                    lssb += blue;

                    lsa += alpha * gm;
                    lsr += red * gm;
                    lsg += green * gm;
                    lsb += blue * gm;
                } else {
                    rsa += alpha * gm;
                    rsr += red * gm;
                    rsg += green * gm;
                    rsb += blue * gm;
                }
            }
            c[j * w] = argb((lsa + rsa) / dv, (lsr + rsr) / dv, (lsg + rsg) / dv, (lsb + rsb) / dv);
            for (int i = 1; i < w; i++) {
                int cc = pixels[j * w + i];
                int ccp = pixels[j * w + (i - 1 - r + pw) % w];
                int ccn = pixels[j * w + (i + r) % w];

                int cca = cc >>> 24;
                int ccr = (cc >> 16) & 0xFF;
                int ccg = (cc >> 8) & 0xFF;
                int ccb = cc & 0xFF;

                int ccpa = ccp >>> 24;
                int ccpr = (ccp >> 16) & 0xFF;
                int ccpg = (ccp >> 8) & 0xFF;
                int ccpb = ccp & 0xFF;

                int ccna = ccn >>> 24;
                int ccnr = (ccn >> 16) & 0xFF;
                int ccng = (ccn >> 8) & 0xFF;
                int ccnb = ccn & 0xFF;

                lsa = lsa - lssa + cca * (r + 1);
                rsa = rsa - cca * r + ccna + ssa - lssa - cca;
                ssa = ssa - ccpa + ccna;
                lssa = lssa - ccpa + cca;

                lsr = lsr - lssr + ccr * (r + 1);
                rsr = rsr - ccr * r + ccnr + ssr - lssr - ccr;
                ssr = ssr - ccpr + ccnr;
                lssr = lssr - ccpr + ccr;

                lsg = lsg - lssg + ccg * (r + 1);
                rsg = rsg - ccg * r + ccng + ssg - lssg - ccg;
                ssg = ssg - ccpg + ccng;
                lssg = lssg - ccpg + ccg;

                lsb = lsb - lssb + ccb * (r + 1);
                rsb = rsb - ccb * r + ccnb + ssb - lssb - ccb;
                ssb = ssb - ccpb + ccnb;
                lssb = lssb - ccpb + ccb;

                c[j * w + i] = argb((lsa + rsa) / dv, (lsr + rsr) / dv, (lsg + rsg) / dv, (lsb + rsb) / dv);
            }
        }
        for (int i = 0; i < w; i++) {
            lsa = lsr = lsg = lsb = rsa = rsr = rsg = rsb = ssa = ssr = ssg = ssb = lssa = lssg = lssb = lssr = 0;
            for (int m = 0; m < n; m++) {
                int cc = c[((m - r + ph) % h) * w + i];
                int alpha = (cc >>> 24);
                int red = ((cc >> 16) & 0xFF);
                int green = ((cc >> 8) & 0xFF);
                int blue = (cc & 0xFF);
                ssa += alpha;
                ssr += red;
                ssg += green;
                ssb += blue;
                int gm = r + 1 - Math.abs(m - r);
                if (m <= r) {
                    lssa += alpha;
                    lssr += red;
                    lssg += green;
                    lssb += blue;

                    lsa += alpha * gm;
                    lsr += red * gm;
                    lsg += green * gm;
                    lsb += blue * gm;
                } else {
                    rsa += alpha * gm;
                    rsr += red * gm;
                    rsg += green * gm;
                    rsb += blue * gm;
                }
            }
            pixels[i] = argb((lsa + rsa) / dv, (lsr + rsr) / dv, (lsg + rsg) /dv, (lsb + rsb) / dv);
            for (int j = 1; j < h; j++) {
                int cc = c[j * w + i];
                int ccp = c[((j - r - 1 + ph) % h) * w + i];
                int ccn = c[((j + r) % h) * w + i];

                int cca = cc >>> 24;
                int ccr = (cc >> 16) & 0xFF;
                int ccg = (cc >> 8) & 0xFF;
                int ccb = cc & 0xFF;

                int ccpa = ccp >>> 24;
                int ccpr = (ccp >> 16) & 0xFF;
                int ccpg = (ccp >> 8) & 0xFF;
                int ccpb = ccp & 0xFF;

                int ccna = ccn >>> 24;
                int ccnr = (ccn >> 16) & 0xFF;
                int ccng = (ccn >> 8) & 0xFF;
                int ccnb = ccn & 0xFF;

                lsa = lsa - lssa + cca * (r + 1);
                rsa = rsa - cca * r + ccna + ssa - lssa - cca;
                ssa = ssa - ccpa + ccna;
                lssa = lssa - ccpa + cca;

                lsr = lsr - lssr + ccr * (r + 1);
                rsr = rsr - ccr * r + ccnr + ssr - lssr - ccr;
                ssr = ssr - ccpr + ccnr;
                lssr = lssr - ccpr + ccr;

                lsg = lsg - lssg + ccg * (r + 1);
                rsg = rsg - ccg * r + ccng + ssg - lssg - ccg;
                ssg = ssg - ccpg + ccng;
                lssg = lssg - ccpg + ccg;

                lsb = lsb - lssb + ccb * (r + 1);
                rsb = rsb - ccb * r + ccnb + ssb - lssb - ccb;
                ssb = ssb - ccpb + ccnb;
                lssb = lssb - ccpb + ccb;

                pixels[j * w + i] = argb((lsa + rsa) / dv, (lsr + rsr) / dv, (lsg + rsg) / dv, (lsb + rsb) / dv);
            }
        }
        long end = System.currentTimeMillis();
        return end - start;
    }

    public static long blur5(int[] in, int width, int height, int r) {
        long start = System.currentTimeMillis();
        int widthMinus = width - 1;
        int heightMinus = height - 1;
        int tableSize = 2 * r + 1;
        int divide[] = new int[256 * tableSize];

        for (int i = 0; i < 256 * tableSize; i++)
            divide[i] = i / tableSize;

        int[] out = new int[width * height];
        for (int y = 0; y < height; y++) {
            int ta = 0, tr = 0, tg = 0, tb = 0;

            for (int i = -r; i <= r; i++) {
                int rgb = in[y * width + Math.max(0, Math.min(widthMinus, i))];
                ta += (rgb >> 24) & 0xff;
                tr += (rgb >> 16) & 0xff;
                tg += (rgb >> 8) & 0xff;
                tb += rgb & 0xff;
            }

            for (int x = 0; x < width; x++) {
                out[x + y * width] = (divide[ta] << 24) | (divide[tr] << 16) | (divide[tg] << 8) | divide[tb];

                int i1 = x + r + 1;
                if (i1 > widthMinus)
                    i1 = widthMinus;
                int i2 = x - r;
                if (i2 < 0)
                    i2 = 0;
                int rgb1 = in[y * width + i1];
                int rgb2 = in[y * width + i2];

                ta += ((rgb1 >> 24) & 0xff) - ((rgb2 >> 24) & 0xff);
                tr += ((rgb1 & 0xff0000) - (rgb2 & 0xff0000)) >> 16;
                tg += ((rgb1 & 0xff00) - (rgb2 & 0xff00)) >> 8;
                tb += (rgb1 & 0xff) - (rgb2 & 0xff);
            }
        }
        for (int x = 0; x < width; x++) {
            int ta = 0, tr = 0, tg = 0, tb = 0;

            for (int i = -r; i <= r; i++) {
                int rgb = out[x + width * Math.max(0, Math.min(heightMinus, i))];
                ta += (rgb >> 24) & 0xff;
                tr += (rgb >> 16) & 0xff;
                tg += (rgb >> 8) & 0xff;
                tb += rgb & 0xff;
            }

            for (int y = 0; y < height; y++) {
                in[x + y * width] = (divide[ta] << 24) | (divide[tr] << 16) | (divide[tg] << 8) | divide[tb];

                int i1 = y + r + 1;
                if (i1 > heightMinus)
                    i1 = heightMinus;
                int i2 = y - r;
                if (i2 < 0)
                    i2 = 0;
                int rgb1 = out[x + i1 * width];
                int rgb2 = out[x + i2 * width];

                ta += ((rgb1 >> 24) & 0xff) - ((rgb2 >> 24) & 0xff);
                tr += ((rgb1 & 0xff0000) - (rgb2 & 0xff0000)) >> 16;
                tg += ((rgb1 & 0xff00) - (rgb2 & 0xff00)) >> 8;
                tb += (rgb1 & 0xff) - (rgb2 & 0xff);
            }
        }
        return System.currentTimeMillis() - start;
    }

    public static long blur6(int[] in, int w, int h, int r) {
        long start = System.currentTimeMillis();
        int dv = 2 * r + 1;
//        int pw = (r / w + 1) * w;
//        int ph = (r / h + 1) * h;
        int sa = 0, sr = 0, sg = 0, sb = 0;
        int inIndex = 0, outIndex = 0;

//        int divide[] = new int[256 * dv];
//        for (int i = 0; i < 256 * dv; i++)
//            divide[i] = i / dv;

        int[] out = new int[w * h];
        for (int y = 0; y < h; y++) {
            sa = sr = sg = sb = 0;
            outIndex = y * w;
            for (int i = -r; i <= r; i++) {
                int rgb = in[inIndex + Math.max(Math.min(w - 1, i), 0)];
                sa += (rgb >>> 24);
                sr += (rgb >> 16) & 0xff;
                sg += (rgb >> 8) & 0xff;
                sb += rgb & 0xff;
            }

            for (int x = 0; x < w; x++) {
                out[outIndex] = argb(sa / dv, sr / dv, sg / dv, sb / dv);
//                out[outIndex] = argb((int) (sa * c), (int) (sr * c), (int) (sg * c), (int) (sb * c));
//                out[outIndex] = argb(divide[sa], divide[sr], divide[sg], divide[sb]);

                int rgb1 = in[y * w + Math.min(x + r + 1, w - 1)];
                int rgb2 = in[y * w + Math.max(x - r, 0)];

                sa += ((rgb1 >> 24) & 0xff) - ((rgb2 >> 24) & 0xff);
                sr += ((rgb1 & 0xff0000) - (rgb2 & 0xff0000)) >> 16;
                sg += ((rgb1 & 0xff00) - (rgb2 & 0xff00)) >> 8;
                sb += (rgb1 & 0xff) - (rgb2 & 0xff);
                outIndex++;
            }
            inIndex += w;
        }
        outIndex = 0;
        for (int x = 0; x < w; x++) {
            sa = sr = sg = sb = 0;
            for (int i = -r; i <= r; i++) {
                int rgb = out[Math.max(0, Math.min(i, h - 1)) * w + x];
                sa += (rgb >>> 24);
                sr += (rgb >> 16) & 0xff;
                sg += (rgb >> 8) & 0xff;
                sb += rgb & 0xff;
            }
            outIndex = x;
            for (int y = 0; y < h; y++) {
                in[outIndex] = argb(sa / dv, sr / dv, sg / dv, sb / dv);
//                in[outIndex] = argb((int) (sa * c), (int) (sr * c), (int) (sg * c), (int) (sb * c));
//                in[outIndex] = argb(divide[sa], divide[sr], divide[sg], divide[sb]);

                int rgb1 = out[Math.min(y + r + 1, h - 1) * w + x];
                int rgb2 = out[Math.max(y - r, 0) * w + x];

                sa += ((rgb1 >> 24) & 0xff) - ((rgb2 >> 24) & 0xff);
                sr += ((rgb1 & 0xff0000) - (rgb2 & 0xff0000)) >> 16;
                sg += ((rgb1 & 0xff00) - (rgb2 & 0xff00)) >> 8;
                sb += (rgb1 & 0xff) - (rgb2 & 0xff);
                outIndex += w;
            }
        }
        return System.currentTimeMillis() - start;
    }

    static int argb(int a, int r, int g, int b) {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static int alpha(int color) {
        return color >>> 24;
    }

    /**
     * Return the red component of a color int. This is the same as saying
     * (color >> 16) & 0xFF
     */
    public static int red(int color) {
        return (color >> 16) & 0xFF;
    }

    /**
     * Return the green component of a color int. This is the same as saying
     * (color >> 8) & 0xFF
     */
    public static int green(int color) {
        return (color >> 8) & 0xFF;
    }

    /**
     * Return the blue component of a color int. This is the same as saying
     * color & 0xFF
     */
    public static int blue(int color) {
        return color & 0xFF;
    }
}
