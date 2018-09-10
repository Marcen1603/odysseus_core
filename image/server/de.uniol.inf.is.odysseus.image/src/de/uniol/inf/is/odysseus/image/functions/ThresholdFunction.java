package de.uniol.inf.is.odysseus.image.functions;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.Objects;

import de.uniol.inf.is.odysseus.image.common.datatype.Image;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.image.common.sdf.schema.SDFImageDatatype;
import de.uniol.inf.is.odysseus.image.util.OpenCVUtil;

/**
 * @author Kristian Bruns
 */
public class ThresholdFunction extends AbstractFunction<Image>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6195886047948415130L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {
		{SDFImageDatatype.IMAGE}, 
		SDFDatatype.NUMBERS,
		{SDFDatatype.BOOLEAN}
	};
	
	public ThresholdFunction() {
		super("threshold", 3, ThresholdFunction.ACC_TYPES, SDFImageDatatype.IMAGE);
	}
	
	@Override
	public Image getValue() {
		final Image image = (Image) this.getInputValue(0);
		double threshold = (double) this.getInputValue(1);
		final boolean dark = (boolean) this.getInputValue(2);
		
		Objects.requireNonNull(image);
		
		Mat iplImage = OpenCVUtil.imageToIplImage(image);
		
		Imgproc.cvtColor(iplImage, iplImage, Imgproc.COLOR_RGB2GRAY);
		
		if (threshold < 1) {
			threshold = this.getThreshold(iplImage, dark);
		}
		
		if (dark == true) {
			Imgproc.threshold(iplImage, iplImage, threshold, 255.0, Imgproc.THRESH_BINARY_INV);
		} else {
			Imgproc.threshold(iplImage, iplImage, threshold, 255.0, Imgproc.THRESH_BINARY);
		}
		
		return OpenCVUtil.iplImageToImage(iplImage, image);
	}
	
	
	protected double getThreshold(Mat image, boolean dark) {
		int[] values = new int[256];
		double[] temp;
		for (int i=0; i < image.width(); i++) {
			for (int j=0; j < image.height(); j++) {
				temp = image.get(i, j);
				values[(int) temp[0]] ++;
			}
		}
		
		int maxLeft = 0;
		int maxRight = 0;
		int left = 0;
		int right = 0;
		for (int i=0; i < values.length; i++) {
			if (i == 0 || values[i] > maxLeft) {
				maxLeft = values[i];
				left = i;
			}
		}
		
		for (int i = (values.length - 1); i >= 0; i--) {
			if (i==(values.length - 1) && (i <= (left - 15) || i > (left + 15))) {
				maxRight = values[i];
				right = i;
			} else if(values[i] > maxRight && (i <= (left - 15) || i >= (left + 15))) {
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
			for (int i=right; i>=left; i--) {
				if (i==right || values[i] < min) {
					min = values[i];
					threshold = i;
				}
			}
		} else {
			for (int i = left; i <= right; i++) {
				if (i == left || values[i] < min) {
					min = values[i];
					threshold = i;
				}
			}
		}
		
		return threshold;
	}
}
