import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;



/*
 * *  PlateSegment Class - This class segments the plate characters from the plates themselves... 
 * 		Assumption: The Input license plate is a confirmed license plate that is detected from the ANN
 */
public class PlateSegment {
	
	private Image licensePlate;
	
	public PlateSegment(Image licensePlate){
		this.setLicensePlate(licensePlate);
	}
	
	public void setLicensePlate(Image licensePlate){ this.licensePlate = licensePlate; }
	public Image getLicensePlate(){ return this.licensePlate; }
	
	public List<Rect> contourToBoundingBoxes(List<MatOfPoint> contours){
		Rect boundingRectangle; List<Rect> boundingBoxes = new ArrayList<Rect>();
		
		for(MatOfPoint contour:contours){ 
			
			//Create a bounding box... :)
			boundingRectangle = Imgproc.boundingRect(contour);
			boundingBoxes.add(boundingRectangle);
			
		}
	
		return boundingBoxes;
	}
	
	public void findPossibleCharacters(){
		// Is is assumed that the plate has been normalized?
		// If it is then we can skip this...SEGMENTATION_THRESHOLD
		
		// --- Threshold the plate :)
		Image image = this.getLicensePlate();
		int width = image.getImageWidth(), height = image.getImageHeight();
		Mat threshold = image.generateThresholdImage(image.equalizeHistogram(), image.SEGMENTATION_THRESHOLD);
		
		// --- Normalize the plate
		// step 1) adjust the plate... (streching) 
		
		// step 2) readjust the plate such that the external dirt is cleaned... 
		//			of course, we crop it :)
	
		int newWidth = (int) (width*0.85), newHeight = (int) (height*0.60); //By 90%;
		int newX = Math.round((width-newWidth)/2);
		int newY = (int) (height*0.13); //Math.round((height-newHeight)/2);
		Rect roi = new Rect(newX, newY, newWidth, newHeight);
		Mat newImage = new Mat(threshold, roi); 
		/*	
		//Generate threshold values for canny :)
		//Histogram h = new Histogram(image); int mean = h.getHistogramMean();
		
		//int low = 100;
		//Mat canny = image.generateCannyEdgeImage(newImage, low, low*3);
		//Imgproc.dilate(canny, canny, new Mat(), new Point(-1, -1), 1);
		*/
		//List<MatOfPoint> list = image.getContourMap(newImage);
		//List<Rect> rectangles = contourToBoundingBoxes(list);

		Mat orig = image.getImage(); 
		Mat testImage = new Mat(orig, roi);
		//Imgproc.drawContours(testImage, list, -1, image.COLOR_RED);
		
		int i = 1;
		/*
		String platePatha = "/media/902A1D4D2A1D31A8/thesis/images/test/local/carrots/";
		for(Rect rectangle:rectangles){
			//Point pt1 = new Point(rectangle.x, rectangle.y),
			//	  pt2 = new Point(rectangle.x+rectangle.width, rectangle.y+rectangle.height);
			//Core.rectangle(testImage, pt1, pt2, image.COLOR_GREEN, 1);
			
			String name = platePatha+"rectangle"+i+".JPG";
			Mat subimage = new Mat(testImage, rectangle);
			image.writeImageToFile(name, subimage);
			
			i++;
		}
		*/
		String platePath = "/media/902A1D4D2A1D31A8/thesis/images/test/local/carrots/croppped.JPG";
		image.writeImageToFile(platePath, newImage);
		
		
	}
	
	
}
