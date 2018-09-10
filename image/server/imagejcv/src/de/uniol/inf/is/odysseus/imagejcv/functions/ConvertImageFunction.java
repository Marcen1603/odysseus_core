package de.uniol.inf.is.odysseus.imagejcv.functions;

import java.util.Objects;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.imagejcv.util.ImageFunctions;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * ImageJCV MEP Function which converts the input image to another pixel format
 * 
 * @author Henrik Surm
 */
public class ConvertImageFunction extends AbstractFunction<ImageJCV> 
{
	private static final long serialVersionUID = 369142604619246125L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] 
			{
				{SDFImageJCVDatatype.IMAGEJCV}, SDFDatatype.NUMBERS
			};
	
	public ConvertImageFunction() 
	{
		super("convertCV", 2, ConvertImageFunction.ACC_TYPES, SDFImageJCVDatatype.IMAGEJCV);
	}
	
	@Override
	public ImageJCV getValue() 
	{
		ImageJCV oldImage = (ImageJCV) getInputValue(0);
		int newPixelFormat = (Integer) getInputValue(1);
		Objects.requireNonNull(oldImage);
		
		return ImageFunctions.convertTo(oldImage, newPixelFormat);
	}

}
