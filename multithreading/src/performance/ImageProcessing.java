package performance;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageProcessing {
    public static final String SRC_FILE = "resources/images/many-flowers.jpg";
    public static final String DEST_FILE = "./out/many-flowers.jpg";

    public static void main(String[] args) throws InterruptedException {
        try {
            BufferedImage originalImage = ImageIO.read(new File(SRC_FILE));
            BufferedImage resultImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);

            long start = System.currentTimeMillis();
            long beforeUsedMem = Runtime.getRuntime().freeMemory();
//             recolorSingleThreaded(originalImage, resultImage);
            int numberOfThreads = 6;
            recolorMultiThreaded(originalImage, resultImage, numberOfThreads);
            long afterUsedMem = Runtime.getRuntime().freeMemory();

            long duration = System.currentTimeMillis() - start;
            System.out.println("Program runs in " + duration + "ms");
            long actualMemUsed = (afterUsedMem - beforeUsedMem) / 1024;
            System.out.println("Before:: " + beforeUsedMem/1024);
            System.out.println("After:: " + afterUsedMem/1024);
            System.out.println("Using:: " + actualMemUsed + " MB");
            File outputFile = new File(DEST_FILE);
            ImageIO.write(resultImage, "jpg", outputFile);
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }

    public static void recolorMultiThreaded(BufferedImage originalImage, BufferedImage resultImage, int numberOfThreads) throws InterruptedException {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight() / numberOfThreads;
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < numberOfThreads; i++) {
            final int threadMultiplier = i;
            Thread thread = new Thread(() -> {
                int leftCorner = 0;
                int topCorner = height * threadMultiplier;
                recolorImage(originalImage, resultImage, leftCorner, topCorner, width, height);
            });
            threads.add(thread);
        }
        threads.forEach(Thread::start);
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
        }
    }

    public static void recolorSingleThreaded(BufferedImage originalImage, BufferedImage resultImage) {
        recolorImage(originalImage, resultImage, 0, 0, originalImage.getWidth(), originalImage.getHeight());
    }

    public static void recolorImage(BufferedImage originalImage, BufferedImage resultImage, int leftCorner, int topCorner, int width, int height) {
        for (int x = leftCorner; x < leftCorner + width && x < originalImage.getWidth(); x++) {
            for (int y = topCorner; y < topCorner + height && y < originalImage.getHeight(); y++) {
                recolorPixel(originalImage, resultImage, x, y);
            }
        }
    }

    public static void recolorPixel(BufferedImage originalImage, BufferedImage resultImage, int x, int y) {
        int rgb = originalImage.getRGB(x, y);

        int red = getRed(rgb);
        int green = getGreen(rgb);
        int blue = getBlue(rgb);

        if (isShadeOfGrey(red, green, blue)) {
            red = Math.min(255, red + 10);
            green = Math.max(0, green - 20);
            blue = Math.max(0, blue - 30);
        }
        int newRgb = createRGBFromColors(red, green, blue);
        setRGB(resultImage, x, y, newRgb);
    }

    private static void setRGB(BufferedImage image, int x, int y, int rgb) {
        image.getRaster().setDataElements(x, y, image.getColorModel().getDataElements(rgb, null));
    }

    /*
        Method to check rgb value is a shade of grey
        Take a particular pixels color values, and determines
        if the pixel is shade of grey
     */
    public static boolean isShadeOfGrey(int red, int green, int blue) {
        // Logic: check all the 3 components have a similar color intensity
        // in other words, if no one particular component is stronger than the rest
        // so if the color is almost a perfect mix of green, red, blue,
        // We know it's a shade of grey
        // The arbitrary distance using to determine their proximity is:: 30
        return Math.abs(red - green) < 30 && Math.abs(red - blue) < 30 && Math.abs(green - blue) < 30;
    }

    /*
        Method taking the individual values of Red, Green, Blue,
         and then building compound pixel RGB out of it
     */
    public static int createRGBFromColors(int red, int green, int blue) {
        int rgb = 0;
        // To add blue value (the right-most value in RGB value),
        // apply logical OR between the RGB target value and the blue value
        rgb |= blue;
        // To add green value
        // shift 1 byte to the left, then add to RGB by applying OR bitwise
        rgb |= green << 8; // => green / 256
        // To add red value
        // shift 2 byte to the left, then add to RGB by applying OR bitwise
        rgb |= red << 16; // => red / (2^16)

        // Set alpha value to the highest value (FF) to make the pixel opaque (not transparent)
        rgb |= 0xFF000000;

        return rgb;
    }

    /*
        Methods to get each individual color from the RGB value
     */
    public static int getRed(int rgb) {
        // Mask out the alpha, green, and blue components
        // Shift the Red value 2 byte to the right
        return (rgb & 0x00FF0000) >> 16; // /2^16
    }

    public static int getGreen(int rgb) {
        // Mask out the alpha, red, and blue components
        // Green is the second byte from the right
        // Shift the Green value 8 bit to the right
        return (rgb & 0x0000FF00) >> 8; // mod (2^8) = 256
    }

    public static int getBlue(int rgb) {
        // Blue is the right-most byte component
        // extract all the blue values out of the pixel
        // apply a bit mask on the pixel,
        // making all the components zero, except the right-most byte (blue component)
        return rgb & 0x000000FF;
        // rgba(0, 0, 0, 0.5)
        //A R G B
        // 0x2938..0F & 0x000000FF => 0x000000-0F
    }
}
