package Image;
import java.awt.image.BufferedImage;

import static Image.Main.THREAD_COUNT;

public class Filter {
    public static final int AVERAGE_9X9_SQUARE = 81;
    public static final int AVERAGE_9X9_HALF_SIZE = 4;
    private BufferedImage image;
    private BufferedImage newImage;

    public Filter(BufferedImage image, BufferedImage newImage) {
        this.image = image;
        this.newImage = newImage;
    }


    public void run() {
        int width = image.getWidth();
        int height = image.getHeight();
        for (int i = 0; i < THREAD_COUNT; i++) {
            Thread t = new Thread(new FilterAverage(height / THREAD_COUNT * i, height / THREAD_COUNT * (i + 1), width, height, image, newImage));
            t.start();
        }
        new FilterAverage(height / THREAD_COUNT * THREAD_COUNT, height, width, height, image, newImage).run();
    }
}