import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;


public class Histogram {

	private Mat histogram;
	private Mat histImage;
	private Image image;
	
	private int[] hist;
	
	// Some final fixed chuchu
	final MatOfFloat histRange = new MatOfFloat(0f, 256f);
	final MatOfInt histSize = new MatOfInt(256);
	boolean accumulate = false;
	
	// For the histogram image... :)
	final int HIST_WIDTH  = 512;
	final int HIST_HEIGHT = 600;
	
	public Histogram(Image image){
		this.image = image;
		
		// Setting up some params :)
		Mat greyscale = image.equalizeHistogram();
		Vector<Mat> bgrPlanes = new Vector<Mat>();
		Core.split(greyscale, bgrPlanes);
		
		Mat hist = new Mat(), histImage = new Mat();
		Imgproc.calcHist(bgrPlanes, new MatOfInt(0), new Mat(), hist, this.histSize, this.histRange, this.accumulate);
		
		// Normalize the histogram :)
	    long bin_w = Math.round((double) (this.HIST_WIDTH/ 256));
	    histImage = new Mat(this.HIST_HEIGHT, this.HIST_WIDTH, CvType.CV_8UC1);
	    Core.normalize(hist, hist, 3, histImage.rows(), Core.NORM_MINMAX);
		
		//this.histogram = hist;
		int[] h = new int[256];
		for (int i = 0; i<256; i++){
			h[i] = (int) Math.round(hist.get(i,0)[0]);
		}
		
		this.histImage = histImage; this.hist = h;
		
	}
	
	public Mat drawHistogram(){
		Mat histImage = this.histImage;
	/*
		for (int i = 1; i < 256; i++) {
			
			long x = Math.round(hist.get(i-1,0)[0]);
			int a = (int) Math.round(hist.get(i-1,0)[0]);
			System.out.println("i is "+i+" and value is:"+Long.toString(x));
			
			
			
	        Core.line(histImage, new Point(bin_w * (i - 1),this.HIST_HEIGHT- Math.round(hist.get( i-1,0)[0])), 
	                new Point(bin_w * (i), this.HIST_HEIGHT-Math.round(Math.round(hist.get(i, 0)[0]))),
	                new Scalar(255, 0, 0), 2, 8, 0);

	    }
	*/	
		return histImage;
	}
	
	public void printHistogram(){
		String histogram = "["; int[] h = this.hist;
		
		for(int i=0; i<h.length; i++){
			histogram += Integer.toString(i)+":"+Integer.toString(h[i])+" ";
		} histogram += "]";
		
		System.out.println(histogram);
		//return this.histogram.dump();
	}
	
	public Mat getHistImage(){ return this.histImage; }
	
	/*
	 *  Some histogram checkers and manipulations...
	 */
	
	public int getLowestHistogramValue(){
		int smallest = 255;
		for(int histValue:this.hist){
			if (histValue < smallest){
				smallest = histValue;
			}
		}
		
		return smallest;
	}
	
	public int getHighestHistogramValue(){
		int highest = 0;
		for(int histValue:this.hist){
			if (histValue > highest){
				highest = histValue;
			}
		}
		
		return highest;
	}
	
	public int getMidPoint(){
		int high = this.getHighestHistogramValue(),
			low  = this.getLowestHistogramValue();
		
		int value = (high-low)/2;
		return value;
	}
	
	public int getHistogramMean(){
		int sum = 0;
		for(int values:this.hist){
			sum += values;
		} 
		
		return sum/this.hist.length;
	}
	
	/*
	 *  Some test driver method... :)
	 */
	
	public void histogramTestRun(){
		//Print the histogram... :)
		printHistogram();
		System.out.println("Highest value is:"+this.getHighestHistogramValue());
		System.out.println("Lowest value is:"+this.getLowestHistogramValue());
		
	}
	
}
