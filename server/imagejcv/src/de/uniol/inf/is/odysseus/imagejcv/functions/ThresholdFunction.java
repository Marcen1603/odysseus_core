 package de.uniol.inf.is.odysseus.imagejcv.functions;

import org.bytedeco.javacpp.opencv_core.IplImage;

import static org.bytedeco.javacpp.opencv_core.*;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;

/**
 * ODYSSEUS Threshold function.
 * 
 * @author Kristian Bruns
 */
public class ThresholdFunction extends AbstractFunction<ImageJCV> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4495729824490355326L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {
		{SDFImageJCVDatatype.IMAGEJCV},
		SDFDatatype.NUMBERS,
		{SDFDatatype.BOOLEAN}
	};
	
	public ThresholdFunction() {
		super("thresholdCV", 3, ThresholdFunction.ACC_TYPES, SDFImageJCVDatatype.IMAGEJCV);
	}
	
	/**
	 * Executes simple threshold for image with threshold value given from input value 1.
	 * If threshold value is smaller than 1 threshold value has to be computed.
	 * In this case boolean value given from input value 2 gets relevance.
	 * 
	 * @author Kristian Bruns
	 * 
	 * @return ImageJCV Image after computing threshold function.
	 */
	@Override
	public ImageJCV getValue() {
		throw new UnsupportedOperationException("This MEP function needs to be re-implemented!");
		
/*		final ImageJCV image = (ImageJCV) this.getInputValue(0);
		double threshold = this.getNumericalInputValue(1);
		boolean dark = (boolean) this.getInputValue(2);		
		Objects.requireNonNull(image);
		
		ImageJCV result = ImageFunctions.toGrayscaleImage(image, true);
		
		if (threshold < 1)
			threshold = getThreshold(result.getImage(), dark);
		
		System.out.println("Threshold: " + threshold);
		
		cvThreshold(result.getImage(), result.getImage(), threshold, 255, dark ? THRESH_BINARY : THRESH_BINARY_INV);
		
		return result;*/
	}
	
	@SuppressWarnings("deprecation")
	protected double getThreshold(IplImage image, boolean dark) {
		int[] values = new int[256];
		int[] temp_values = new int[256];
		double value;
		CvMat matImage = new CvMat();
		cvGetMat(image, matImage);
		for (int i=0; i < image.height(); i++) {
			boolean in = false;
			for (int j=0; j < image.width(); j++) {
				value = matImage.get(i, j);
				temp_values[(int) value] ++;
				if ((int) value == 125 && in == false) {
					in = true;
				} else if ((int) value == 125 && in == true) {
					in = false;
				}
				
				if (in == true) {
					values[(int) value] ++;
				}
			}
		}
		
		if (temp_values[125] == 0) {
			values = temp_values;
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
			if (i == (values.length - 1) && (i <= (left - 15) || i > (left + 15))) {
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
			for (int i = right; i >= left; i--) {
				if (i == right || values[i] < min) {
					min = values[i];
					threshold = i;
				}
			}
		} else {
			for (int i = left; i <= right; i++) {
				if (i== left || values[i] < min) {
					min = values[i];
					threshold = i;
				}
			}
		}
		
		return threshold;
	}
}
