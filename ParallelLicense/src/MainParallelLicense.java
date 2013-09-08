import java.util.ArrayList;
import java.util.List;

import javax.swing.WindowConstants;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class MainParallelLicense {
	
	static ParallelLicenseMainUI mainUI;

	public static void main(String[] args) {
		System.out.println("Welcome to OpenCV "+Core.VERSION);
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		mainUI = new ParallelLicenseMainUI();
		mainUI.setSize(mainUI.getMaximumSize());
		mainUI.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		mainUI.setVisible(true);
		
		//testRun();
		//mainUI.setVisible(true);
		
	}
	
	public static void testRun(){
		String filePath = "/media/902A1D4D2A1D31A8/thesis/images/test/local/source0.JPG";
		String returnPath = "/media/902A1D4D2A1D31A8/thesis/images/test/local/carrots/source.JPG";
		
		String platePath = "/media/902A1D4D2A1D31A8/thesis/images/test/local/carrots/5a.JPG";
		Image img = new Image(platePath); //String path; int i = 1;	
		PlateSegment plate = new PlateSegment(img);
		plate.findPossibleCharacters();
		
		//List<Mat> candidates = img.generateCandidatePlates();

	}
	
	public void threading(){
		// For MultiThreading... :)
				List<Thread> threads = new ArrayList<Thread>();
				/*
				for(Mat candidate:candidates){
					//path = returnPath+i+".JPG";
					Image candidateImage = new Image("");
					candidateImage.setImage(candidate);
					
					PlateDetect cand = new PlateDetect(candidateImage);
					cand.setProcessName(returnPath+i+".JPG");
					Thread worker = new Thread(cand);
					
					worker.start();
					threads.add(worker);
					//img.writeImageToFile(path, candidate);
					i++;
					
				} */
		
	}

}
