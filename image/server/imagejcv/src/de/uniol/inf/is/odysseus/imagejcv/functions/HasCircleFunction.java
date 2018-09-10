package de.uniol.inf.is.odysseus.imagejcv.functions;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;

/**
 *  ODYSSEUS Function that identifies circles in an image.
 * 
 * @author Kristian Bruns
 */
public class HasCircleFunction extends AbstractFunction<Boolean> {

	private static final long serialVersionUID = -8225840218354217604L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {
		{SDFImageJCVDatatype.IMAGEJCV},
	};
	
	public HasCircleFunction() {
		super("hasCircleCV", 1, HasCircleFunction.ACC_TYPES, SDFImageJCVDatatype.IMAGEJCV);
	}
	
	/**
	 * If a circle exists in the image from input value 0 return true, else return false.
	 * 
	 * @author Kristian Bruns
	 * 
	 * @return Boolean Image has circles or not?
	 */
	@Override
	public Boolean getValue() {
		throw new UnsupportedOperationException("This MEP function needs to be re-implemented!");
		
/*		final ImageJCV image = (ImageJCV) this.getInputValue(0);
		
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
		}*/
	}
	

}
