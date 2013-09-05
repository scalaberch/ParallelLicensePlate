
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.*;
import org.opencv.imgproc.*;


public class Image {

	// This is a BGR Mat image... :)
	private Mat image;
	private String IMAGE_WINDOW_NAME;
	
	// Some color constants... :)
	final private Scalar COLOR_GREEN = new Scalar(0, 255, 0);
	final private Scalar COLOR_RED = new Scalar(0, 0, 255);
	final private Scalar COLOR_BLUE = new Scalar(255, 0, 0);
	
	/* 
	 * Constructor Image()
	 * @param filename: the filename itself.
	 * Assumption: image is 640x480
	 */
	
	public Image(String filename){
		if (!filename.isEmpty()){
			Mat img = Highgui.imread(filename);
			if (img.empty()){
				System.out.println("File not found or is empty!");
			} else {
				// Store to attribute image.
				this.setImage(img);
				this.IMAGE_WINDOW_NAME = filename;
			}
		}
	}
	
	/* 
	 * Getter getImage()
	 * Gets the Mat image attribute
	 */
	public Mat getImage() { 
		return image; 
	}

	
	public void setImage(Mat image) { 
		this.image = image; 
	}
	
	
	public String printImageMatrix(){
		Mat image = this.getImage();
		return image.dump();
	}
	
	public boolean isImageGreyScale(Mat image){
		//Mat image = this.getImage();
		
		if (image.channels() == 1){
			return true;
		} else { return false; }
	}
	
	public Mat generateGreyScaleImage(){
		Mat greyscale = new Mat();
		Imgproc.cvtColor(this.getImage(), greyscale, Imgproc.COLOR_BGR2GRAY);
		
		return greyscale;
	}
	
	public Mat equalizeHistogram(){
		Mat blackNWhite = this.generateGreyScaleImage(), equalized = new Mat();
		Imgproc.equalizeHist(blackNWhite, equalized);
		
		return equalized;
	}
	
	public Mat generateThresholdImage(Mat image, int thresholdValue){
		Mat result = new Mat(); int type = Imgproc.THRESH_BINARY; //+Imgproc.THRESH_OTSU
		//Imgproc.threshold(image, result, thresholdValue, 255, type);
		Imgproc.adaptiveThreshold(image, result, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, type, 11, 18);
		
		return result;
	}
	
	public Mat generateCannyEdgeImage(Mat thresholded, double thresh1, double thresh2){
		Mat result = new Mat();
		Imgproc.Canny(thresholded, result, thresh1, thresh2);
		//Imgproc.dilate(result, result, new Mat(), new Point(1, 1), 1);
		
		return result;
	}
	
	public int getThresholdValue(){
		// Get histogram... :)
		Histogram h = this.getHistogram();
		
		// For now, we use the midpoint value of the histogram :)
		return 120;
	}
	
	public Histogram getHistogram(){
		return new Histogram(this);
	}
	
	public List<MatOfPoint> getContourMap(Mat edgeMap){ 
		// Assumption: Image has been generated as a thresholded image... :)
		//	Also, the image has been processed for Canny Edge Detection... :)
		
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>(), filteredContours;
		Imgproc.findContours(edgeMap, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
		
		return contours;
	}
	
	public boolean rectangleIsAcceptable(Rect rectangle){
		double area = rectangle.height*rectangle.width;
		return area > 1500 && area < 5000;
	}
	
	public List<Rect> contourToBoundingBoxes(List<MatOfPoint> contours){
		double percentage = 0.02; // 2% lang muna gays :)
		
		// Then, we filter the contours... :)
		double contourLength = 0; MatOfPoint2f curve, approxCurve = new MatOfPoint2f();
		Rect boundingRectangle; List<Rect> boundingBoxes = new ArrayList<Rect>();
		
		for(MatOfPoint contour:contours){ 
			
			curve = new MatOfPoint2f(contour.toArray());
			contourLength = Imgproc.arcLength(curve, true);
			Imgproc.approxPolyDP(curve, approxCurve, percentage*contourLength, true);
			
			//Create a bounding box... :)
			boundingRectangle = Imgproc.boundingRect(contour);
			if (this.rectangleIsAcceptable(boundingRectangle)){
				boundingBoxes.add(boundingRectangle);
			}	
			
		}
	
		return boundingBoxes;
	}
	
	public List<Rect> generateCandidateRectangles(){
		Mat equalized, thresholded, canny;
		
		equalized = this.equalizeHistogram();
		thresholded = this.generateThresholdImage(equalized, this.getThresholdValue());
		
		//Generate threshold values for canny :)
		Histogram h = new Histogram(this); int mean = h.getHistogramMean();
		canny = this.generateCannyEdgeImage(thresholded, mean*1.33, mean*0.66);
		List<Rect> rectangles = this.contourToBoundingBoxes(this.getContourMap(canny));
		
		return rectangles;
	}
	
	public List<Mat> generateCandidatePlates(){
		List<Mat> candidates = new ArrayList<Mat>();
		List<Rect> rectangles = this.generateCandidateRectangles();
		
		//Iterate thru the boxes to crop on the image... :)
		Mat originalImage = this.getImage(), croppedImage;
		for(Rect rectangle:rectangles){
			//Okay, let's crop some from the images :)
			croppedImage = new Mat(originalImage, rectangle);
			candidates.add(croppedImage);
		}
		
		//this.setImage(originalImage);
		return candidates;
	}
	
	public void writeImageToFile(String filename, Mat image){
		Highgui.imwrite(filename, image);
		System.out.println("File written to "+filename);
	}

	public Mat generateImageOfDrawingRectangles(){
		List<Rect> rectangles = this.generateCandidateRectangles();
		Mat originalImage = this.getImage();
		
		for(Rect rectangle:rectangles){
			Point pt1 = new Point(rectangle.x, rectangle.y),
				  pt2 = new Point(rectangle.x+rectangle.width, rectangle.y+rectangle.height);
			Core.rectangle(originalImage, pt1, pt2, this.COLOR_GREEN, 2);
		}
		
		return originalImage;
	}
	
}