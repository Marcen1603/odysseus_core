package de.uniol.inf.is.odysseus.imagejcv.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * ODYSSEUS function for setting one pixel value in an image.
 * 
 * @author Kristian Bruns
 */
public class SetImageFunction extends AbstractFunction<ImageJCV> {

	private static final long serialVersionUID = 5617318633630685561L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {
		{SDFImageJCVDatatype.IMAGEJCV},
		SDFDatatype.NUMBERS,
		SDFDatatype.NUMBERS,
		SDFDatatype.NUMBERS
	};
	
	public SetImageFunction() {
		super("setCV", 4, SetImageFunction.ACC_TYPES, SDFImageJCVDatatype.IMAGEJCV);
	}

	/**
	 * Sets pixel value of one pixel in the image given by input value 0.
	 * 
	 * @author Kristian Bruns
	 * 
	 * @return ImageJCV Image with setted pixel value.
	 */
	@Override
	public ImageJCV getValue() {
		throw new UnsupportedOperationException("This MEP function needs to be re-implemented!");
		
/*		final ImageJCV image = (ImageJCV) this.getInputValue(0);
		final int x = this.getNumericalInputValue(1).intValue();
		final int y = this.getNumericalInputValue(2).intValue();
		final int value = this.getNumericalInputValue(3).intValue();
		
		Objects.requireNonNull(image);
		
		// TODO: It is really inefficient to create a whole copy when only one pixel is changed.
		// Maybe use something else than the Map-Operator which requires that each function which changes an operand creates a copy of it?
		ImageJCV result = new ImageJCV(image);
		IplImage iplImage = result.getImage();
		
		int index = y * iplImage.widthStep() + x * iplImage.nChannels();
		
		result.set(index, value);
		
		return result;*/
	}
	
}
