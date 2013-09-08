import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class PlateUI extends JFrame{

	public PlateUI(Image img){
		super("Some example for that yow!");
		Container content = getContentPane();
		
		DrawingArea originalImage = new DrawingArea(img);
		content.add(originalImage, BorderLayout.WEST);
		
		DrawingArea newImage = new DrawingArea(new Image(""));
		content.add(newImage, BorderLayout.WEST);
		
		this.pack();
		setVisible(true);
	}
	
}

class DrawingArea extends JPanel {
	
	Image img;
	
	DrawingArea(Image img){
		this.img = img;
	}
	
	@Override
	public void paintComponent(Graphics g){
		Image img = this.img;
		BufferedImage image = img.convertMatToBufferedImage(img.getImage());
		g.drawImage(image, 0, 0, null);
	}
}
