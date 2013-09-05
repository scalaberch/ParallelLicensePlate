import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class MainParallelLicense {

	public static void main(String[] args) {
		System.out.println("Welcome to OpenCV "+Core.VERSION);
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		testRun();
		

	}
	
	public static void testRun(){
		String filePath = "/media/902A1D4D2A1D31A8/thesis/images/test/local/source0.JPG";
		String returnPath = "/media/902A1D4D2A1D31A8/thesis/images/test/local/carrots/";
		
		Image img = new Image(filePath); String path; int i = 1;
		List<Mat> candidates = img.generateCandidatePlates();
		
		// For MultiThreading... :)
		List<Thread> threads = new ArrayList<Thread>();
		
		for(Mat candidate:candidates){
			//path = returnPath+i+".JPG";
			PlateDetect cand = new PlateDetect(candidate);
			cand.setProcessName(returnPath+i+".JPG");
			Thread worker = new Thread(cand);
			
			
			worker.start();
			threads.add(worker);
			//img.writeImageToFile(path, candidate);
			i++;
			
		}
		//img.writeImageToFile(returnPath, img.getImage());
		
	}

}
