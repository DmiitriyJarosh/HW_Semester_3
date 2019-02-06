package com.jenkov;

import java.awt.image.BufferedImage;


//import static com.jenkov.Main.THREAD_COUNT;

public class Filter {
    public static final int AVERAGE_9X9_SQUARE = 81;
    public static final int AVERAGE_9X9_HALF_SIZE = 4;
    private BufferedImage image;
    private BufferedImage newImage;

    public Filter(BufferedImage image, BufferedImage newImage) {
        this.image = image;
        this.newImage = newImage;
    }


    public void run(boolean flag, int numOfThreads) {
        int width = image.getWidth();
        int height = image.getHeight();
        for (int i = 0; i < numOfThreads; i++) {
            if (flag) {
                Thread t = new Thread(new FilterAverageHorizontal(height / numOfThreads * i, height / numOfThreads * (i + 1), width, height, image, newImage));
                t.start();
            } else {
                Thread t = new Thread(new FilterAverageVertical(width / numOfThreads * i, width / numOfThreads * (i + 1), width, height, image, newImage));
                t.start();
            }

        }
        if (flag) {
            new FilterAverageHorizontal(height / numOfThreads * numOfThreads, height, width, height, image, newImage).run();
        } else {
            new FilterAverageVertical(width / numOfThreads * numOfThreads, width, width, height, image, newImage).run();
        }
    }
}