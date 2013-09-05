import org.opencv.core.Mat;



public class PlateDetect implements Runnable{
	
	private Image candidatePlate;
	private String processName; //for debugging purposes only :)
	
	public PlateDetect(Mat candidatePlate){
		Image img = new Image("");
		img.setImage(candidatePlate);
		this.setCandidatePlate(img);
	}
	
	public Image getCandidatePlate(){ return this.candidatePlate; }
	public void setCandidatePlate(Image plate){ this.candidatePlate = plate; }
	
	//for debugging purposes only
	public void setProcessName(String name){ this.processName = name; }
	public String getProcessName(){ return this.processName; }

	@Override
	public void run() {
		// Convert image to threshold muna...
		Image img = this.getCandidatePlate(); 
		Mat thresholded = img.generateThresholdImage(img.equalizeHistogram(), img.getThresholdValue());
		
		// Test for thresholding :)
		img.writeImageToFile(this.getProcessName(), img.getImage());
		
		// Set the images to a specific size...
		// Size is about 70x25 (wxh)
		// Check first if it is landscape?
		
		// Then slice the image into two...
		
		// Then feed it to the neural network...
		
		
		
	}

	
	
}
