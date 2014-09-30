package de.uniol.inf.is.odysseus.imagejcv.functions;

import java.util.Objects;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
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
	
	@Override
	public ImageJCV getValue() {
		final ImageJCV image = (ImageJCV) this.getInputValue(0);
		final double value = this.getNumericalInputValue(1);
		
		Objects.requireNonNull(image);
		
		image.fill((int) value);
		return image;
	}
}
