package t;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;

import javax.imageio.ImageIO;

public class PreviewGenerator {

    private File wallpaper;
    private File[] icons;
    private File weather;
    private File dir;

    private int width = 1080, height = 1920;

    public PreviewGenerator(File wallpaperFile, File[] icons, File weather, File targetDir) {
        this.wallpaper = wallpaperFile;
        this.icons = icons;
        this.weather = weather;
        this.dir = targetDir;
    }

    public void run() throws Exception {
        BufferedImage drawImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D canvas = drawImage.createGraphics();

        drawWallpaper(canvas);

        LinkedList<File> iconList = new LinkedList<File>(Arrays.asList(icons));
        for (int i = 0; i < 20; i++) {
            File icon = iconList.poll();
            if (icon == null) {
                break;
            }
            int x, y;
            if (i < 4) {
                x = (2 * i + 1) * width / 8;
                y = height * 9 / 10;
            } else {
                int index = i - 4;
                x = (2 * (index % 4) + 1) * width / 8;
                y = (2 * (index / 4) + 1) * height / 10;
            }
            BufferedImage iconImage = ImageIO.read(icon);
            canvas.drawImage(iconImage, x - iconImage.getWidth() / 2, y - iconImage.getHeight() / 2, null);
        }

        drawScreenIndicator(canvas);

        canvas.dispose();

        ImageIO.write(drawImage, "JPG", new File(dir, "preview1.jpg"));
    }

    private void drawWallpaper(Graphics2D canvas) throws Exception {
        BufferedImage wallpaperImage = ImageIO.read(wallpaper);
        int wallpaperWidth = wallpaperImage.getWidth();
        int wallpaperHeight = wallpaperImage.getHeight();
        float aspectRatio = Math.min(wallpaperWidth * 1.0f / width, wallpaperHeight * 1.0f / height);
        int scaledWidth = (int) (width * aspectRatio);
        int scaledHeight = (int) (height * aspectRatio);
        int x = (wallpaperWidth - scaledWidth) / 2;
        int y = (wallpaperHeight - scaledHeight) / 2;
        canvas.drawImage(wallpaperImage, 0, 0, width, height, x, y, x + scaledWidth, y + scaledHeight, null);
    }

    private void drawScreenIndicator(Graphics2D canvas) throws Exception {
        canvas.setColor(Color.CYAN);
        canvas.fillRoundRect(width / 2 - 10, height * 4 / 5 - 10, 20, 20, 20, 20);
        canvas.fillRoundRect(width / 2 - 36, height * 4 / 5 - 8, 16, 16, 16, 16);
        canvas.fillRoundRect(width / 2 + 20, height * 4 / 5 - 8, 16, 16, 16, 16);
    }
}
