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


    public void run(boolean flag) {
        int width = image.getWidth();
        int height = image.getHeight();
        for (int i = 0; i < THREAD_COUNT; i++) {
            if (flag) {
                Thread t = new Thread(new FilterAverageHorizontal(height / THREAD_COUNT * i, height / THREAD_COUNT * (i + 1), width, height, image, newImage));
                t.start();
            } else {
                Thread t = new Thread(new FilterAverageVertical(width / THREAD_COUNT * i, width / THREAD_COUNT * (i + 1), width, height, image, newImage));
                t.start();
            }

        }
        if (flag) {
            new FilterAverageHorizontal(height / THREAD_COUNT * THREAD_COUNT, height, width, height, image, newImage).run();
        } else {
            new FilterAverageVertical(width / THREAD_COUNT * THREAD_COUNT, width, width, height, image, newImage).run();
        }
    }
}