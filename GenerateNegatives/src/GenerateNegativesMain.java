import java.io.File;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.highgui.Highgui;




public class GenerateNegativesMain {
	
	// Some constants of the images... :)
	private static final int _outputImgWidth  = 1024;
	private static final int _outputImgHeight = 768;
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Welcome to OpenCV "+Core.VERSION);
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		// Main Path directory...
		String mainPath = "/media/902A1D4D2A1D31A8/thesis/images_convert_to_negative/";
		
		// Traversing to the file system... :)
		// Use this on the final run
		File[] images = new File(mainPath.concat("/source/")).listFiles(); 
		
		//Image img;
		//String[] filenames = new String[]{"DSC_0241.JPG", "DSC_0242.JPG"};		
		
		//for (String filename: filenames){
		for(File image: images){
			System.out.println("Reading image file...");
			
			img = new Image(mainPath.concat("/source/")+image.getName());
			Mat subImage = new Mat();
			
			// Do the LOOP
			int horizontalBase = 0, verticalBase = 0,
				horizontalOffset = _outputImgWidth,
				verticalOffset   = _outputImgHeight;
			
			int subImages = 0; String outputName;
			
			while(verticalOffset < img.getImgHeight()){
				
				// Loop horizontally :)
				while(horizontalOffset < img.getImgWidth()){
					subImage = img.sliceImage(horizontalBase, verticalBase,
												horizontalOffset, verticalOffset);
					
					outputName = filename+"_sub_"+Integer.toString(subImages); subImages++;

					// Write to a file...
					Highgui.imwrite(mainPath.concat("/results/"+outputName+".JPG"),subImage);
					
					// skip for 10 pixels :)
					horizontalOffset += 100; horizontalBase += 100;
				}
				System.out.println("Done line...");
				
				// Reset the variables :)
				horizontalBase = 0; horizontalOffset = _outputImgWidth;
				// Increment the vertical values by 10 pixels again :)
				verticalBase += 100; verticalOffset += 100;
			}
			
		}

	}
}


class Image {
	
	private Mat image;
	
	// Constructor
	public Image(String filename){
		if (!filename.isEmpty() || filename != null){
			Mat img = Highgui.imread(filename);
			if (img.empty()){
				this.setImage(new Mat());
				System.out.println("File not found or is empty!");
			} else {
				// Store to attribute image.
				this.setImage(img);
			}
		}
	}

	// Getter and Setter Methods
	public Mat getImage(){ return this.image; }
	public void setImage(Mat img){ this.image = img; }
	
	public int getImgWidth(){
		return this.image.width();
	}
	
	public int getImgHeight(){
		return this.image.height();
	}
	
	// Slicer Method :)
	public Mat sliceImage(int x, int y, int width, int height){
		Mat image = this.image.clone();
		return new Mat(image, new Rect(x, y, width, height));
		
	}
	
}
