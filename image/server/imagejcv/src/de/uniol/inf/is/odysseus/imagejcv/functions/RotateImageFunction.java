package de.uniol.inf.is.odysseus.imagejcv.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * ODYSSEUS Function for rotating an image.
 * 
 * @author Kristian Bruns
 *
 */
public class RotateImageFunction extends AbstractFunction<ImageJCV> {

	private static final long serialVersionUID = -5180144587236353923L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {
		{SDFImageJCVDatatype.IMAGEJCV}, SDFDatatype.NUMBERS
	};
	
	public RotateImageFunction() {
		super("rotateCV", 2, RotateImageFunction.ACC_TYPES, SDFImageJCVDatatype.IMAGEJCV);
	}
	
	/**
	 * Rotates an image by given angle from input value 1.
	 * 
	 * @author Kristian Bruns
	 * 
	 * @return ImageJCV Rotated image.
	 */
	@Override
	public ImageJCV getValue() {
		throw new UnsupportedOperationException("This MEP function needs to be re-implemented!");
		
/*		final ImageJCV image = (ImageJCV) this.getInputValue(0);
		final double angle = this.getNumericalInputValue(1);		
		Objects.requireNonNull(image);
				
		final CvPoint2D32f center = new CvPoint2D32f();
		center.x(image.getWidth() / 2);
		center.y(image.getHeight() / 2);
		
		final CvMat mapMatrix = cvCreateMat(2, 3, CV_32FC1);
		cv2DRotationMatrix(center, angle, 1.0, mapMatrix);

		ImageJCV result = ImageJCV.createCompatible(image);
		cvWarpAffine(image.getImage(), result.getImage(), mapMatrix);		
		return result;*/
	}
}
