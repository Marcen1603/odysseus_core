package de.uniol.inf.is.odysseus.image.functions;

import java.util.Objects;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.image.common.datatype.Image;
import de.uniol.inf.is.odysseus.image.common.sdf.schema.SDFImageDatatype;
import de.uniol.inf.is.odysseus.image.util.OpenCVUtil;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Core;

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
		Mat circles = new Mat();
		
		Imgproc.cvtColor(iplImage, iplGray, Imgproc.COLOR_RGB2GRAY);
		
		circles = this.getBoundary(iplGray);
		
		for (int x=0; x < circles.cols(); x++) {
			double vCircle[] = circles.get(0, x);
			
			Point center = new Point(Math.round(vCircle[0]), Math.round(vCircle[1]));
			int radius = (int) Math.round(vCircle[2]);
			Core.circle(iplGray, center, radius, new Scalar(125, 125, 125), 1, 8, 0);
		}
		
		double threshold = this.getThreshold(iplGray, dark);
		
		if (dark == true) {
			Imgproc.threshold(iplGray, iplGray, threshold, 255.0, Imgproc.THRESH_BINARY_INV);
		} else {
			Imgproc.threshold(iplGray, iplGray, threshold, 255.0, Imgproc.THRESH_BINARY);
		}
		
		
		for (int x=0; x < circles.cols(); x++) {
			double vCircle[] = circles.get(0, x);
			
			Point center = new Point(Math.round(vCircle[0]), Math.round(vCircle[1]));
			int radius = (int) Math.round(vCircle[2]);
			Core.circle(iplGray, center, radius, new Scalar(125, 125, 125), 1, 8, 0);
		}
		
		double percent = this.getPixelAmount(iplGray);
		
		System.out.println("Prozent:" + (percent * 100));
		
		return percent * 100;
	}
	
	
	/**
	 * Computes Threshold value for an image.
	 * 
	 * @param image Image.
	 * 
	 * @return Threshold value.
	 */
	protected double getThreshold(Mat image, boolean dark) {
		
		int[] values = new int[256];
		double[] temp;
		for(int i=0; i < image.width(); i++) {
			boolean in = false;
			for (int j=0; j < image.height(); j++) {
				temp = image.get(i, j);
				if ((int) temp[0] == 125 && in == false) {
					in = true;
				} else if ((int) temp[0] == 125 && in == true) {
					in = false;
				}
				
				if (in == true) {
					values[(int) temp[0]] ++;
				}
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
			if (i == (values.length - 1) && (i <= (left - 15) || i >= (left + 15))) {
				maxRight = values[i];
				right = i;
			} else if (values[i] > maxRight && (i <= (left - 15) || i >= (left + 15))) {
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
	protected double getPixelAmount(Mat image) {
		double[] temp;
		int weiss = 0;
		int schwarz = 0;
	    int gesamt = 0;
	    boolean in;
		for (int i=0; i < image.width(); i++) {
			in = false;
			for (int j=0; j < image.height(); j++) {
				temp = image.get(i, j);
				
				if (in == true) {
					gesamt ++;
				}
				
				if ((int) temp[0] == 125 && in == false) {
					in = true;
				} else if ((int) temp[0] == 125 && in == true) {
					in = false;
				}
				
				if ((int) temp[0] == 255 && in == true) {
					weiss ++;
				} else if((int) temp[0] == 0 && in == true) {
					schwarz ++;
				}
			}
		}
		
		return ((double) schwarz / (double) gesamt);
	}	
	
	protected Mat getBoundary(Mat image) {
		Mat circles = new Mat();
		
		Imgproc.HoughCircles(image, circles, Imgproc.CV_HOUGH_GRADIENT, 3d, (image.width() * image.height()));
		
		return circles;
	}

}
