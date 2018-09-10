package de.uniol.inf.is.odysseus.imagejcv.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * ODYSSEUS Function for filling all pixels of an image with the same color.
 * 
 * @author Kristian Bruns
 */
public class FillImageFunction extends AbstractFunction<ImageJCV> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1405286100908841368L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {{SDFImageJCVDatatype.IMAGEJCV}, SDFDatatype.NUMBERS};
	
	public FillImageFunction() {
		super("fillCV", 2, FillImageFunction.ACC_TYPES, SDFImageJCVDatatype.IMAGEJCV);
	}
	
	/**
	 * Fills all pixels of an image with the color from input Value 1.
	 * 
	 * @author Kristian Bruns
	 * 
	 * @return ImageJCV Image filled with color from input Value 1.
	 */
	@Override
	public ImageJCV getValue() {
		throw new UnsupportedOperationException("This MEP function needs to be re-implemented!");
		
/*		final ImageJCV image = (ImageJCV) this.getInputValue(0);
		final double value = this.getNumericalInputValue(1);
		
		Objects.requireNonNull(image);
		
		image.fill((int) value);
		return image;*/
	}
}
