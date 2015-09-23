package de.uniol.inf.is.odysseus.imagejcv.functions;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.avutil.*;

import java.nio.ByteBuffer;
import java.util.Objects;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class Convert16BitToRGBFunction extends AbstractFunction<ImageJCV> {

	private static final long serialVersionUID = -7606324725154709923L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] 
			{
				{SDFImageJCVDatatype.IMAGEJCV}
			};
	
	public Convert16BitToRGBFunction() {
		super("convert16BitToRGBCV", 1, Convert16BitToRGBFunction.ACC_TYPES, SDFImageJCVDatatype.IMAGEJCV);
	}
	
	/**
	 * Encodes a 1-channel 16-bit grayscale image to a 4-channel BGRA image. 
	 * 2 pixels of the original image are combined into one pixel of the output image.
	 * Input image width has to be a multiple of 2, and output image width is halved. 
	 */
	@Override
	public ImageJCV getValue() 
	{
		ImageJCV oldImage = (ImageJCV) this.getInputValue(0);
		Objects.requireNonNull(oldImage);
		
		if ((oldImage.getDepth() != IPL_DEPTH_16S) && (oldImage.getDepth() != IPL_DEPTH_16U))
			throw new UnsupportedOperationException("convert16BitToRGBCV requires an image with 16 bit depth");
		
		if (oldImage.getWidth() % 2 != 0)
			throw new UnsupportedOperationException("Input image width is no multiple of 2");
		
		ImageJCV newImage = new ImageJCV(oldImage.getWidth() / 2, oldImage.getHeight(), IPL_DEPTH_8U, 4, AV_PIX_FMT_RGBA);
		
		ByteBuffer oldBuffer = oldImage.getImageData();
		ByteBuffer newBuffer = newImage.getImageData();
		
		if (newBuffer.limit() != oldBuffer.limit())
			throw new UnsupportedOperationException("Image buffers are not the same size...?");
		
		if (newImage.getWidthStep() == oldImage.getWidthStep())
		{
			newBuffer.put(oldBuffer);
		}
		else
			throw new UnsupportedOperationException("Not implemented yet!");

		return ImageJCV.extendToMultipleOf(newImage, 2);
	}

}
