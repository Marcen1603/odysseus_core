package de.uniol.inf.is.odysseus.imagejcv.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * ODYSSEUS Function that inverses an image.
 * 
 * @author Kristian Bruns
 */
public class InverseImageFunction extends AbstractFunction<ImageJCV> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6995742582650184331L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {{SDFImageJCVDatatype.IMAGEJCV}};
	
	public InverseImageFunction() {
		super("invCV", 1, InverseImageFunction.ACC_TYPES, SDFImageJCVDatatype.IMAGEJCV);
	}
	
	/**
	 * Inverses each pixel of an image by subtracting it from 255.
	 * 
	 * @author Kristian Bruns
	 * 
	 * @return ImageJCV Inversed image.
	 */
	@Override
	public ImageJCV getValue() {
		throw new UnsupportedOperationException("This MEP function needs to be re-implemented!");
		
/*		final ImageJCV image = (ImageJCV) this.getInputValue(0);
		Objects.requireNonNull(image);
		
		ImageJCV result = new ImageJCV(image);		
		final IplImage iplImage = result.getImage();
		
		CvMat matImage = new CvMat();
		cvGetMat(iplImage, matImage);
		
		double value;
		for (int i=0; i < iplImage.width(); i++) {
			for (int j=0; j < iplImage.height(); j++) {
				value = matImage.get(i, j);
				matImage.put(i, j, 255 - value);
			}
			
		}
		
		cvGetImage(matImage, iplImage);
		
		return result;*/
	}
	
}
