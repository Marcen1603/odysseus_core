package de.uniol.inf.is.odysseus.imagejcv.functions;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * ODYSSEUS function for creating an image.
 * 
 * @author Kristian Bruns
 */
public class ToImageFunction extends AbstractFunction<ImageJCV> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 282271378681582656L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {SDFDatatype.NUMBERS, SDFDatatype.NUMBERS};
	
	public ToImageFunction() {
		super("toImageCV", 2, ToImageFunction.ACC_TYPES, SDFImageJCVDatatype.IMAGEJCV);
	}
	
	/**
	 * Creates an image with width and height given by the input values 0 and 1.
	 * 
	 * @author Kristian Bruns
	 * 
	 * @return ImageJCV Created image.
	 */
	@Override
	public ImageJCV getValue() {
		final int width = this.getNumericalInputValue(0).intValue();
		final int height = this.getNumericalInputValue(1).intValue();
		Preconditions.checkArgument(width > 0, "Invalid Dimension");
		Preconditions.checkArgument(height > 0, "Invalid Dimension");
		return new ImageJCV(width, height);
	}
	
}
