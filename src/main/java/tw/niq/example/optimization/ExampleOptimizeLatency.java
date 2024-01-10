package tw.niq.example.optimization;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

@Profile("ExampleOptimizeLatency")
@Component
public class ExampleOptimizeLatency implements CommandLineRunner {
	
	public static final String SOURCE_FILE = "classpath:images/many-flowers.jpg";
	public static final String DESTINATION_FILE = "./out/many-flowers.jpg";

	@Override
	public void run(String... args) throws Exception {
		
		// 建立原始暫存檔案
		BufferedImage originalImage = ImageIO.read(ResourceUtils.getFile(SOURCE_FILE));
		
		// 建立目標暫存檔案
		BufferedImage resultImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		long startTime = System.currentTimeMillis();
		
//		recolorSingleThreaded(originalImage, resultImage);
		
        int numberOfThreads = 4;
        recolorMultithreaded(originalImage, resultImage, numberOfThreads);
		
		long endTime = System.currentTimeMillis();
		
		long duration = endTime - startTime;
		
		// 建立目標檔案
		File outputFile = new File(DESTINATION_FILE);
		
		// 寫入目標檔案
		ImageIO.write(resultImage, "jpg", outputFile);
		
		System.out.println("duration: " + String.valueOf(duration));
	}
	
	public static void recolorMultithreaded(BufferedImage originalImage, BufferedImage resultImage, int numberOfThreads) {
		List<Thread> threads = new ArrayList<>();
		int width = originalImage.getWidth();
		int height = originalImage.getHeight() / numberOfThreads;

		for (int i = 0; i < numberOfThreads; i++) {
			final int threadMultiplier = i;

			Thread thread = new Thread(() -> {
				int xOrigin = 0;
				int yOrigin = height * threadMultiplier;

				recolorImage(originalImage, resultImage, xOrigin, yOrigin, width, height);
			});

			threads.add(thread);
		}

		for (Thread thread : threads) {
			thread.start();
		}

		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
			}
		}
	}
	
	public static void recolorSingleThreaded(BufferedImage originalImage, BufferedImage resultImage) {
		recolorImage(originalImage, resultImage, 0, 0, originalImage.getWidth(), originalImage.getHeight());
	}

	public static void recolorImage(BufferedImage originalImage, BufferedImage resultImage, int leftCorner,
			int topCorner, int width, int height) {
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

		int newRed;
		int newGreen;
		int newBlue;

		if (isShadeOfGray(red, green, blue)) {
			newRed = Math.min(255, red + 10);
			newGreen = Math.max(0, green - 80);
			newBlue = Math.max(0, blue - 20);
		} else {
			newRed = red;
			newGreen = green;
			newBlue = blue;
		}
		int newRGB = createRGBFromColors(newRed, newGreen, newBlue);
		setRGB(resultImage, x, y, newRGB);
	}

	public static void setRGB(BufferedImage image, int x, int y, int rgb) {
		image.getRaster().setDataElements(x, y, image.getColorModel().getDataElements(rgb, null));
	}

	public static boolean isShadeOfGray(int red, int green, int blue) {
		return Math.abs(red - green) < 30 && Math.abs(red - blue) < 30 && Math.abs(green - blue) < 30;
	}

	public static int createRGBFromColors(int red, int green, int blue) {
		int rgb = 0;

		rgb |= blue;
		rgb |= green << 8;
		rgb |= red << 16;

		rgb |= 0xFF000000;

		return rgb;
	}

	public static int getRed(int rgb) {
		return (rgb & 0x00FF0000) >> 16;
	}

	public static int getGreen(int rgb) {
		return (rgb & 0x0000FF00) >> 8;
	}

	public static int getBlue(int rgb) {
		return rgb & 0x000000FF;
	}

}
