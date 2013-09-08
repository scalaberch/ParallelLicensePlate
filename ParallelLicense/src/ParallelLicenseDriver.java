import java.util.List;

import org.opencv.core.Mat;




public class ParallelLicenseDriver {

	Image inputImage;
	Image outputImage;
	
	// Some Constants...
	final int SEQUENTIAL_PROCESSING = 0;
	final int PARALLEL_PROCESSING   = 1;
	
	ParallelLicenseDriver(){
		inputImage = null;
	}
	
	public void setInputImage(Image image){
		this.inputImage = image;
	}
	
	public Image getInputImage(){
		return this.inputImage;
	}
	
	public Image getOutputImage(){
		return this.outputImage;
	}
	
	public void openInputImageFile(String fileName){
		this.setInputImage(new Image(fileName));
	}
	
	public void executeScanImage(int scanType){
		Image im = this.getInputImage();
		
		if (scanType == this.SEQUENTIAL_PROCESSING){
			executeSequentialProcessing(im);
		}
	}
	
	public void executeSequentialProcessing(Image inputImage){
		List<Mat> candidates = inputImage.generateCandidatePlates();
		
		Image output = new Image("");
		output.setImage(inputImage.generateImageOfDrawingRectangles());
		this.outputImage = output;
	}
	
}
