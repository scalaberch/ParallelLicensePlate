package shadowstrider;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

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
//                plateLetters += (i < 3) ?  Character.toString((char)(r.nextInt(26) + 65)) :  Integer.toString(r.nextInt(10));
            	plateLetters += (i < 3) ?  letterList[r.nextInt(4)] :  numList[r.nextInt(2)];
            }
            if (!listOfPlates.contains(plateLetters)) {
                listOfPlates.add(plateLetters);
                BufferedImage loadImg = Loadimage.loadImage(System.getProperty("user.dir") + "/src/shadowstrider/resources/template2.png");
                BufferedImage[] plateChars = new BufferedImage[6];
                plateChars[0] = Loadimage.loadImage(System.getProperty("user.dir") + "/src/shadowstrider/resources/" + Character.toString(plateLetters.charAt(0)) + ".png");
                plateChars[1] = Loadimage.loadImage(System.getProperty("user.dir") + "/src/shadowstrider/resources/" + Character.toString(plateLetters.charAt(1)) + ".png");
                plateChars[2] = Loadimage.loadImage(System.getProperty("user.dir") + "/src/shadowstrider/resources/" + Character.toString(plateLetters.charAt(2)) + ".png");
                plateChars[3] = Loadimage.loadImage(System.getProperty("user.dir") + "/src/shadowstrider/resources/" + Character.toString(plateLetters.charAt(3)) + ".png");
                plateChars[4] = Loadimage.loadImage(System.getProperty("user.dir") + "/src/shadowstrider/resources/" + Character.toString(plateLetters.charAt(4)) + ".png");
                plateChars[5] = Loadimage.loadImage(System.getProperty("user.dir") + "/src/shadowstrider/resources/" + Character.toString(plateLetters.charAt(5)) + ".png");
				BufferedImage generatedPlate = generateMaskedImage(loadImg,plateChars);
//				frame.setBounds(0, 0, loadImg.getWidth(), loadImg.getHeight());
//				lpane.setBounds(0, 0, loadImg.getWidth(), loadImg.getHeight());
//				JImagePanel panel = new JImagePanel(generatedPlate, 0, 0);
//				frame.add(panel);
//				frame.setVisible(true);
				writeImage(generatedPlate, System.getProperty("user.dir") + "/src/shadowstrider/generatedPlatesOutput/generatedPlate" + Integer.toString(ctr + 1) + ".png", "png");
				ctr++;
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
        g.drawImage(plateChars[0],(int) (plateTemplate.getWidth()/15.74), (int)(plateTemplate.getHeight()/6.74), null);
        g.drawImage(plateChars[1],(int) (plateTemplate.getWidth()/4.89), (int)(plateTemplate.getHeight()/6.74), null);
        g.drawImage(plateChars[2],(int) (plateTemplate.getWidth()/2.92), (int)(plateTemplate.getHeight()/6.74), null);
        g.drawImage(plateChars[3],(int) (plateTemplate.getWidth()/1.74), (int)(plateTemplate.getHeight()/6.85), null);
        g.drawImage(plateChars[4],(int) (plateTemplate.getWidth()/1.405), (int)(plateTemplate.getHeight()/6.85), null);
        g.drawImage(plateChars[5],(int) (plateTemplate.getWidth()/1.176), (int)(plateTemplate.getHeight()/6.85), null);
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
