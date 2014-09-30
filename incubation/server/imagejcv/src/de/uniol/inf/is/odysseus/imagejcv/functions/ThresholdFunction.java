package de.uniol.inf.is.odysseus.imagejcv.functions;

import org.bytedeco.javacpp.opencv_core.IplImage;

import java.nio.ByteBuffer;
import java.util.Objects;

import static org.bytedeco.javacpp.opencv_imgproc.*;

import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;

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
	
	@Override
	public ImageJCV getValue() {
		final ImageJCV image = (ImageJCV) this.getInputValue(0);
		double threshold = this.getNumericalInputValue(1);
		final boolean dark = (boolean) this.getInputValue(2);
		
		Objects.requireNonNull(image);
		
		IplImage iplImage = image.getImage();
		
		cvCvtColor(iplImage, iplImage, CV_BGR2GRAY);
		
		if (threshold < 1) {
			threshold = this.getThreshold(iplImage, dark);
		}
		
		if (dark == true) {
			cvThreshold(iplImage, iplImage, threshold, 255, THRESH_BINARY_INV);
		} else {
			cvThreshold(iplImage, iplImage, threshold, 255, THRESH_BINARY);
		}
		
		image.setImage(iplImage);
		
		return image;
	}
	
	protected double getThreshold(IplImage image, boolean dark) {
		int[] values = new int[256];
		int value = 0;
		ByteBuffer buffer = image.getByteBuffer();
		for (int i=0; i < image.width(); i++) {
			for (int j=0; j < image.height(); j++) {
				int index = j * image.widthStep() + i * image.nChannels();
				value = buffer.getInt(index);
				values[value] ++;
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
