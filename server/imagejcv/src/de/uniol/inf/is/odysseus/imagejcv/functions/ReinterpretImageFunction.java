package de.uniol.inf.is.odysseus.imagejcv.functions;

import java.util.Objects;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.imagejcv.util.ImageFunctions;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * ImageJCV MEP Function which reinterprets the content of an image (argument 0) as 
 * another image with different width (argument 1),  height (argument 2), depth (argument 3), 
 * number of channels (argument 4) or pixel format (argument 5). Can be used to address an 
 * 16-bit grayscale image as an 32-bit RGBA image with half of the width.
 * Buffer sizes of original and reinterpreted image must match!
 *
 * @author Henrik Surm
 */
public class ReinterpretImageFunction extends AbstractFunction<ImageJCV> 
{
	private static final long serialVersionUID = 6738356190389554113L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] 
			{
				{SDFImageJCVDatatype.IMAGEJCV}, SDFDatatype.NUMBERS, 
				SDFDatatype.NUMBERS, SDFDatatype.NUMBERS, 
				SDFDatatype.NUMBERS, SDFDatatype.NUMBERS 
			};
	
	public ReinterpretImageFunction() 
	{
		super("reinterpretCV", 6, ReinterpretImageFunction.ACC_TYPES, SDFImageJCVDatatype.IMAGEJCV);
	}
	
	@Override public ImageJCV getValue() 
	{
		ImageJCV oldImage = (ImageJCV) getInputValue(0);
		int newWidth = ((Number) getInputValue(1)).intValue();
		int newHeight = ((Number) getInputValue(2)).intValue();
		int newDepth = ((Number) getInputValue(3)).intValue();
		int newNumChannels = ((Number) getInputValue(4)).intValue();
		int newPixelFormat = ((Number) getInputValue(5)).intValue();
		
		Objects.requireNonNull(oldImage);
		
		if (newWidth == -1) newWidth = oldImage.getWidth();
		if (newHeight == -1) newHeight = oldImage.getHeight();
		if (newDepth == -1) newDepth = oldImage.getDepth();
		if (newNumChannels == -1) newNumChannels = oldImage.getNumChannels();
		if (newPixelFormat == -1) newPixelFormat = oldImage.getPixelFormat();
		
		return ImageFunctions.reinterpret(oldImage, newWidth, newHeight, newDepth, newNumChannels, newPixelFormat);
	}
}
