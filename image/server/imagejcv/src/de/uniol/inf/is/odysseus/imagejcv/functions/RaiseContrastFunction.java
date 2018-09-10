package de.uniol.inf.is.odysseus.imagejcv.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * ODYSSEUS Function for raising the contrast of an image.
 * 
 * @author Kristian Bruns
 *
 */
public class RaiseContrastFunction extends AbstractFunction<ImageJCV> {

	private static final long serialVersionUID = -7606324725154709923L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {
		{SDFImageJCVDatatype.IMAGEJCV},
		{SDFDatatype.INTEGER}
	};
	
	public RaiseContrastFunction() {
		super("contrastCV", 2, RaiseContrastFunction.ACC_TYPES, SDFImageJCVDatatype.IMAGEJCV);
	}
	
	/**
	 * Raises the contrast of an image by value from input value 1.
	 * The contrast is raised by multiplying value with each pixel.
	 * 
	 * @author Kristian Bruns
	 * 
	 * @return ImageJCV Image with raised contrast.
	 */
	@Override
	public ImageJCV getValue() {
		throw new UnsupportedOperationException("This MEP function needs to be re-implemented!");
		
/*		final ImageJCV image = (ImageJCV) this.getInputValue(0);
		final int value = this.getNumericalInputValue(1).intValue();
		
		Objects.requireNonNull(image);
		
		ImageJCV result = new ImageJCV(image);
		IplImage iplImage = result.getImage();
		
		CvMat matImage = new CvMat();
		cvGetMat(iplImage, matImage);
		
		cvScale(matImage, matImage, value, 0);
		
		cvGetImage(matImage, iplImage);
		
		return result;*/
	}

}
