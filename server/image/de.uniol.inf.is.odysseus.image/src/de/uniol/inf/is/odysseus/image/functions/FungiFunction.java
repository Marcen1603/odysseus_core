package de.uniol.inf.is.odysseus.image.functions;

import java.util.Objects;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.image.common.datatype.Image;
import de.uniol.inf.is.odysseus.image.common.sdf.schema.SDFImageDatatype;
import de.uniol.inf.is.odysseus.image.util.OpenCVUtil;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * 
 * @author Kristian Bruns <kristian.bruns@uni-oldenburg.de>
 *
 */
public class FungiFunction extends AbstractFunction<Double>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2822397085424970101L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {
		{SDFImageDatatype.IMAGE}, 
		{SDFDatatype.BOOLEAN}
	};
	
	public FungiFunction() {
		super("fungi", 2, FungiFunction.ACC_TYPES, SDFImageDatatype.IMAGE);
	}
	
	@Override
	public Double getValue() {
		final Image image = (Image) this.getInputValue(0);
		final boolean dark = (boolean) this.getInputValue(1);
		
		Objects.requireNonNull(image);
		
		double val;
		int imageVal;
		Image grayImage = new Image(image.getWidth(), image.getHeight());
		for (int i=0; i<image.getWidth(); i++){
			for (int j=0; j<image.getHeight(); j++) {
				val = image.get(i, j);
				imageVal = (int) Math.round(val/65793);
				grayImage.set(i, j, imageVal);
			}
		}
		
		Mat iplImage = OpenCVUtil.imageToIplImage(grayImage);
		Mat iplGray = new Mat();
		
		Imgproc.cvtColor(iplImage, iplGray, Imgproc.COLOR_BGR2GRAY);
		
		double threshold = this.getThreshold(grayImage, dark);
		
		Image threshImage = new Image(grayImage.getWidth(), grayImage.getHeight());
		
		threshImage = this.threshold(grayImage, threshold, dark);
		
		threshImage = this.getBoundary(threshImage);
		
		double percent = this.getPixelAmount(threshImage);
		
		System.out.println("Prozent:" + (percent * 100));
		
		return (percent * 100);
	}
	
	
	/**
	 * Computes Threshold value for an image.
	 * 
	 * @param image Image.
	 * 
	 * @return Threshold value.
	 */
	protected double getThreshold(Image image, boolean dark) {
		int[] values = new int[256];
		for(int i=0; i < image.getWidth(); i++) {
			for (int j=0; j < image.getHeight(); j++) {
				values[image.get(i, j)] ++;
			}
		}
		
		int maxLeft = 0;
		int maxRight = 0;
		int left = 0;
		int right = 0;
		for (int i=0; i < values.length; i++) {
			if (i == 0) {
				maxLeft = values[i];
				left = i;
			} else if (values[i] > maxLeft) {
				maxLeft = values[i];
				left = i;
			}
			
		}
		
		for(int i=(values.length - 1); i >= 0; i--) {
			if (i == (values.length - 1) && (i <= (left - 5) || i >= (left + 5))) {
				maxRight = values[i];
				right = i;
			} else if (values[i] > maxRight && (i <= (left - 5) || i >= (left + 5))) {
				maxRight = values[i];
				right = i;
			}
		}
		
		//If right < left switch right and left.
		int tmpMaxRight = 0;
		if (right < left) {
			tmpMaxRight = left;
			left = right;
			right = tmpMaxRight;
		}
		
		int min = 0;
		double threshold = 0.0;
		if (dark == true) {
			for (int i=right; i >= left; i--) {
				if (i == right) {
					min = values[i];
					threshold = i;
				} else if (values[i] < min) {
					min = values[i];
					threshold = i;
				}
			}
		} else {
			for(int i=left; i <= right; i++) {
				if (i == left) {
					min = values[i];
					threshold = i;
				} else if (values[i] < min) {
					min = values[i];
					threshold = i;
				}
			}
		}
		
		
		System.out.println("Threshold: " + threshold);
		
		return threshold;
	}
	
	/**
	 * Function that calculates percentage of fungi.
	 * 
	 * @param image Image.
	 * 
	 * @return double Percentage of fungi. 
	 */
	protected double getPixelAmount(Image image) {
		double temp;
		@SuppressWarnings("unused")
		int weiss = 0;
		int schwarz = 0;
	    int gesamt = (image.getWidth() * image.getHeight());
		for (int i=0; i < image.getWidth(); i++) {
			for (int j=0; j < image.getHeight(); j++) {
				temp = image.get(i, j);
				if ((int) temp == 255) {
					weiss ++;
				} else if((int) temp == 0) {
					schwarz ++;
				}
			}
		}
		
		return ((double) schwarz / (double) gesamt);
	}
	
	
	/**
	 * Executes threshold for image.
	 * 
	 * @param image Image.
	 * @param threshold Threshold value
	 * @param dark Is Fungi dark or not?
	 * 
	 * @return
	 */
	protected Image threshold(Image image, double threshold, boolean dark) {
		for (int i=0; i<image.getWidth(); i++) {
			for (int j=0; j<image.getHeight(); j++) {
				System.out.println("Threshold: " + threshold + " Value: " + image.get(i, j));
				if (image.get(i, j) <= threshold && dark == true) {
					image.set(i, j, 0);
				} else if (image.get(i, j) <= threshold && dark == false) {
					image.set(i, j, 255);
				} else if (image.get(i, j) > threshold && dark == true) {
					image.set(i,j, 255);
				} else {
					image.set(i, j, 0);
				}
			}
		}
		
		return image;
	}
	
	
	protected Image getBoundary(Image image) {
		Mat iplImage = OpenCVUtil.imageToIplImage(image);
		Mat circles = new Mat();
		
		Imgproc.HoughCircles(iplImage, circles, Imgproc.CV_HOUGH_GRADIENT, 1d, (iplImage.height()/4));
		
		System.out.println("Circles: " + circles.cols());
		
		return OpenCVUtil.iplImageToImage(iplImage, image);
	}

}
