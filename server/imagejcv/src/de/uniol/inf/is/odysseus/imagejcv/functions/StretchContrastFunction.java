package de.uniol.inf.is.odysseus.imagejcv.functions;

import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_16S;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_16U;

import java.nio.ByteBuffer;
import java.util.Objects;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
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
		
		if ((oldImage.getDepth() != IPL_DEPTH_16S) && (oldImage.getDepth() != IPL_DEPTH_16U))
			throw new UnsupportedOperationException("StretchContrast not implemented for images with depth != 16!");
		
		ImageJCV newImage = new ImageJCV(oldImage.getWidth(), oldImage.getHeight(), IPL_DEPTH_8U, 3);		

//		long lastTime = System.nanoTime();
		
		ByteBuffer oldBuffer = oldImage.getImageData();
		ByteBuffer newBuffer = newImage.getImageData();
		
		for(int y = 0; y < oldImage.getHeight(); y++)
		for(int x = 0; x < oldImage.getWidth(); x++) 
		{
			int oldIndex = y * oldImage.getWidthStep() + x * oldImage.getNumChannels()*2;
			int newIndex = y * newImage.getWidthStep() + x * newImage.getNumChannels();

			double value = oldBuffer.getShort(oldIndex);
			int newVal = (int)( (double)(value - oldMin) / (oldMax - oldMin) * (newMax - newMin) + newMin);
			if (newVal < 0) newVal = 0;
			if (newVal > 255) newVal = 255;
			
	        // Sets the pixel to a value (RGB, stored in BGR order).
	        newBuffer.put(newIndex, 		(byte) (newVal));
	        newBuffer.put(newIndex + 1, 	(byte) (newVal));
	        newBuffer.put(newIndex + 2, 	(byte) (newVal));
		}		
		
/*		long curTime = System.nanoTime();
		System.out.println("dt = " + (curTime - lastTime) / 1.0e6 + "ms");*/		

		return newImage;
	}

}
