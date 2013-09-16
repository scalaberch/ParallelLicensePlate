package shadowstrider;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import parallelplates.Image;

public class GeneratePlates {

	public void loadAndDisplayImage(JFrame frame) {
		System.out.println("Plates will be output at: " + System.getProperty("user.dir") + "/src/shadowstrider/generatedPlateOutputs");
		System.out.println("Working..");
		long startTime = System.currentTimeMillis();
		Random r = new Random();
        TreeSet<String> listOfPlates = new TreeSet<String>();
        int ctr = 0;
        while(ctr < 69) {
		    JLayeredPane lpane = new JLayeredPane();
            String plateLetters = "";
            String[] letterList = {"Z", "F", "C", "K"};
            String[] numList = {"0","8"};
            for (int i = 0; i < 6; i++) {
                plateLetters += (i < 3) ?  Character.toString((char)(r.nextInt(26) + 65)) :  Integer.toString(r.nextInt(10));
//            	plateLetters += (i < 3) ?  letterList[r.nextInt(4)] :  numList[r.nextInt(2)];
            }
            if (!listOfPlates.contains(plateLetters)) {
                listOfPlates.add(plateLetters);
                BufferedImage loadImg = Loadimage.loadImage(System.getProperty("user.dir") + "/src/shadowstrider/resources/new_letters/TEMPLATE_NEW.png");
                BufferedImage[] plateChars = new BufferedImage[6];
                plateChars[0] = Loadimage.loadImage(System.getProperty("user.dir") + "/src/shadowstrider/resources/newer/" + Character.toString(plateLetters.charAt(0)) + ".png");
                plateChars[1] = Loadimage.loadImage(System.getProperty("user.dir") + "/src/shadowstrider/resources/newer/" + Character.toString(plateLetters.charAt(1)) + ".png");
                plateChars[2] = Loadimage.loadImage(System.getProperty("user.dir") + "/src/shadowstrider/resources/newer/" + Character.toString(plateLetters.charAt(2)) + ".png");
                plateChars[3] = Loadimage.loadImage(System.getProperty("user.dir") + "/src/shadowstrider/resources/newer/" + Character.toString(plateLetters.charAt(3)) + ".png");
                plateChars[4] = Loadimage.loadImage(System.getProperty("user.dir") + "/src/shadowstrider/resources/newer/" + Character.toString(plateLetters.charAt(4)) + ".png");
                plateChars[5] = Loadimage.loadImage(System.getProperty("user.dir") + "/src/shadowstrider/resources/newer/" + Character.toString(plateLetters.charAt(5)) + ".png");
				BufferedImage generatedPlate = generateMaskedImage(loadImg,plateChars);
//				frame.setBounds(0, 0, loadImg.getWidth(), loadImg.getHeight());
//				lpane.setBounds(0, 0, loadImg.getWidth(), loadImg.getHeight());
//				JImagePanel panel = new JImagePanel(generatedPlate, 0, 0);
//				frame.add(panel);
//				frame.setVisible(true);
				ctr++;
				System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//				writeImage(generatedPlate, System.getProperty("user.dir") + "/src/shadowstrider/generatedPlatesOutput/" + plateLetters + ".png", "png");
//				Image thresholder = new Image(System.getProperty("user.dir") + "/src/shadowstrider/generatedPlatesOutput/" + plateLetters + ".png");
//				Mat equalized = thresholder.equalizeHistogram();
//				writeImage(thresholder.convertMatToBufferedImage(thresholder.generateThresholdImage(equalized, thresholder.ADAPTIVE_THRESHOLD)), System.getProperty("user.dir") + "/src/shadowstrider/generatedPlatesOutput/" + plateLetters + ".png", "png");
				Image thresholder = new Image();
				byte[] pixels = ((DataBufferByte) generatedPlate.getRaster().getDataBuffer()).getData();
				Mat generatedPlateMat = new Mat(generatedPlate.getHeight(), generatedPlate.getWidth(), CvType.CV_8UC3);
				generatedPlateMat.put(0, 0, pixels);
				thresholder.setImage(generatedPlateMat);
				Mat equalized = thresholder.generateGreyScaleImage();
//				writeImage(thresholder.convertMatToBufferedImage(thresholder.generateThresholdImage(equalized, thresholder.ADAPTIVE_THRESHOLD)), System.getProperty("user.dir") + "/src/shadowstrider/generatedPlatesOutput/" + plateLetters + ".png", "png");
				writeImage(thresholder.convertMatToBufferedImage(equalized), System.getProperty("user.dir") + "/src/shadowstrider/generatedPlatesOutput/" + plateLetters + ".png", "png");
				System.out.println(Integer.toString(ctr) + ((ctr > 1) ? " plates generated" : " plate generated") );
            }
        }
        System.out.println("Done");
        long endTime = System.currentTimeMillis();
        System.out.println(Integer.toString(ctr) + " plates generated in " + (endTime - startTime) + " milliseconds");
	}
	
	public static void main(String[] args) {
		GeneratePlates ia = new GeneratePlates();
		JFrame frame = new JFrame("License Plate");
		ia.loadAndDisplayImage(frame);
	}
	
	public static BufferedImage generateMaskedImage(BufferedImage plateTemplate, BufferedImage[] plateChars) {
		Graphics2D g = plateTemplate.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(plateTemplate, 0, 0, null);
        g.drawImage(plateChars[0],(int) (plateTemplate.getWidth()/15.74), (int)(plateTemplate.getHeight()/6.74), 118, 235, null);
        g.drawImage(plateChars[1],(int) (plateTemplate.getWidth()/4.89), (int)(plateTemplate.getHeight()/6.74), 118, 235, null);
        g.drawImage(plateChars[2],(int) (plateTemplate.getWidth()/2.92), (int)(plateTemplate.getHeight()/6.74), 118, 235, null);
        g.drawImage(plateChars[3],(int) (plateTemplate.getWidth()/1.74), (int)(plateTemplate.getHeight()/6.85), 118, 235,  null);
        g.drawImage(plateChars[4],(int) (plateTemplate.getWidth()/1.405), (int)(plateTemplate.getHeight()/6.85), 118, 235,  null);
        g.drawImage(plateChars[5],(int) (plateTemplate.getWidth()/1.176), (int)(plateTemplate.getHeight()/6.85), 118, 235, null);
        g.dispose();
		return plateTemplate;
	}
	
    public static void writeImage(BufferedImage img, String fileLocation,
            String extension) {
        try {
            BufferedImage bi = img;
            File outputfile = new File(fileLocation);
            ImageIO.write(bi, extension, outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
