package de.uniol.inf.is.odysseus.imagejcv.functions;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

import java.util.Objects;

import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;

/**
 * 
 * @author Kristian Bruns
 *
 */
public class HasCircleFunction extends AbstractFunction<Boolean> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8225840218354217604L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {
		{SDFImageJCVDatatype.IMAGEJCV},
	};
	
	public HasCircleFunction() {
		super("hasCircleCV", 1, HasCircleFunction.ACC_TYPES, SDFImageJCVDatatype.IMAGEJCV);
	}
	
	@Override
	public Boolean getValue() {
		final ImageJCV image = (ImageJCV) this.getInputValue(0);
		
		Objects.requireNonNull(image);
		
		IplImage iplImage = image.getImage();
		
		CvMemStorage mem = CvMemStorage.create();
		
		if (iplImage.nChannels() > 1) {
			cvCvtColor(iplImage, iplImage, CV_BGR2GRAY);
		}
		
		CvSeq circles = cvHoughCircles(iplImage, mem, CV_HOUGH_GRADIENT, 3d, (iplImage.width() * iplImage.height()));
		
		if (circles.total() > 0) {
			return true;
		} else {
			return false;
		}
	}
	

}
