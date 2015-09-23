package de.uniol.inf.is.odysseus.imagejcv.functions;

import java.util.Objects;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.imagejcv.util.ImageFunctions;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class StretchContrastFunction extends AbstractFunction<ImageJCV> {

	private static final long serialVersionUID = -7606324725154709923L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] 
			{
				{SDFImageJCVDatatype.IMAGEJCV}, {SDFDatatype.INTEGER}, {SDFDatatype.INTEGER}, {SDFDatatype.INTEGER}, {SDFDatatype.INTEGER}
			};
	
	public StretchContrastFunction() {
		super("stretchContrastCV", 5, StretchContrastFunction.ACC_TYPES, SDFImageJCVDatatype.IMAGEJCV);
	}
	
	/**
	 * Stretches the contrast of a 1-channel 16-bit grayscale image and converts it to a 3-channel BGR image. 
	 * Parameters of the function are (oldMin, oldMax, newMin, newMax)
	 */
	@Override
	public ImageJCV getValue() 
	{
		ImageJCV oldImage = (ImageJCV) this.getInputValue(0);
		Objects.requireNonNull(oldImage);
		
		int oldMin = this.getNumericalInputValue(1).intValue();
		int oldMax = this.getNumericalInputValue(2).intValue();
		int newMin = this.getNumericalInputValue(3).intValue();
		int newMax = this.getNumericalInputValue(4).intValue();
		
		return ImageFunctions.stretchContrast(oldImage, oldMin, oldMax, newMin, newMax);
	}

}
