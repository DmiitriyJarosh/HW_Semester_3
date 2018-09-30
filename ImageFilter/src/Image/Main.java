package Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static final int THREAD_COUNT = 4;
    public static void main(String[] args) {
        File bmpFile = new File("1.bmp");
        try {
            BufferedImage image = ImageIO.read(bmpFile);
            ImageIO.write(image, "bmp", new File("2.bmp"));
            BufferedImage newImage = ImageIO.read(new File("2.bmp"));
            System.out.println("Enter something when all " + (THREAD_COUNT + 1) + " threads will finish");
            new Filter(image, newImage).run(false);
            System.in.read();
            ImageIO.write(newImage, "bmp", new File("2.bmp"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
