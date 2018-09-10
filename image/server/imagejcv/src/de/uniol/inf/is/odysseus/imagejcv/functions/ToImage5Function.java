package de.uniol.inf.is.odysseus.imagejcv.functions;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * ODYSSEUS function for creating an image.
 * 
 * @author Henrik Surm
 */
public class ToImage5Function extends AbstractFunction<ImageJCV> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 282271378681582656L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {SDFDatatype.NUMBERS, SDFDatatype.NUMBERS, SDFDatatype.NUMBERS, SDFDatatype.NUMBERS, SDFDatatype.NUMBERS};
	
	public ToImage5Function() {
		super("toImageCV", 5, ToImage5Function.ACC_TYPES, SDFImageJCVDatatype.IMAGEJCV);
	}
	
	/**
	 * Creates an image with width and height given by the input values 0 and 1, 
	 * depth and channels given by input values 2 and 3
	 * 
	 * @return ImageJCV Created image.
	 */
	@Override
	public ImageJCV getValue() {
		final int width = this.getNumericalInputValue(0).intValue();
		final int height = this.getNumericalInputValue(1).intValue();
		final int depth = this.getNumericalInputValue(2).intValue();
		final int channels = this.getNumericalInputValue(3).intValue();		
		final int pixelFormat = this.getNumericalInputValue(4).intValue();
		Preconditions.checkArgument(width > 0, "Invalid width");
		Preconditions.checkArgument(height > 0, "Invalid height");
		
		return new ImageJCV(width, height, depth, channels, pixelFormat);
	}
	
}
