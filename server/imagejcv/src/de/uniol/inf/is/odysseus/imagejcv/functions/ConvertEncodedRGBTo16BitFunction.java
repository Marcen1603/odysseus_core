package de.uniol.inf.is.odysseus.imagejcv.functions;

import java.util.Objects;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.imagejcv.util.ImageFunctions;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class ConvertEncodedRGBTo16BitFunction extends AbstractFunction<ImageJCV> {

	private static final long serialVersionUID = -7606324725154709923L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] 
			{
				{SDFImageJCVDatatype.IMAGEJCV}
			};
	
	public ConvertEncodedRGBTo16BitFunction() {
		super("convertEncodedRGBTo16BitCV", 1, ConvertEncodedRGBTo16BitFunction.ACC_TYPES, SDFImageJCVDatatype.IMAGEJCV);
	}
	
	/**
	 * Decodes a 4-channel RGBA image into a 1-channel 16-bit grayscale image 
	 */
	@Override
	public ImageJCV getValue() 
	{
		ImageJCV oldImage = (ImageJCV) this.getInputValue(0);
		Objects.requireNonNull(oldImage);		
		return ImageFunctions.convertEncodedRGBTo16Bit(oldImage);
	}

}
