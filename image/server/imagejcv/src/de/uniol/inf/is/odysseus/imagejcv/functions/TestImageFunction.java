package de.uniol.inf.is.odysseus.imagejcv.functions;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.avutil.*;

import java.nio.ByteBuffer;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * ODYSSEUS function for creating a test image.
 * 
 * @author Henrik Surm
 */
public class TestImageFunction extends AbstractFunction<ImageJCV> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 282271378681582656L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {SDFDatatype.NUMBERS, SDFDatatype.NUMBERS, SDFDatatype.NUMBERS};
	
	public TestImageFunction() {
		super("testImageCV", 3, TestImageFunction.ACC_TYPES, SDFImageJCVDatatype.IMAGEJCV);
	}
	
	/**
	 * Creates an image with width and height given by the input values 0 and 1, and a filling parameter 2.
	 */
	@Override
	public ImageJCV getValue() {
		int width = this.getNumericalInputValue(0).intValue();
		int height = this.getNumericalInputValue(1).intValue();
		double fill = this.getNumericalInputValue(2).doubleValue();
		Preconditions.checkArgument(width > 0, "Invalid Dimension");
		Preconditions.checkArgument(height > 0, "Invalid Dimension");
		
		ImageJCV newImage = new ImageJCV(width, height, IPL_DEPTH_16S, 1, AV_PIX_FMT_GRAY16);
		
		ByteBuffer newBuffer = newImage.getImageData();
		
/*		int min = -32768;
		int max = 32767;
		double freq = 0.01;
		
		for(int y = 0; y < newImage.getHeight(); y++)
		for(int x = 0; x < newImage.getWidth(); x++) 
		{
			int newIndex = y * newImage.getWidthStep() + x * newImage.getNumChannels() * 2;

			double d = ((x + y + fill) * freq) % 1.0;
			
			short val = (short) (min + (max - min) * d);
			
	        newBuffer.putShort(newIndex, Short.reverseBytes((short) val));
		}*/

		short cur = (short) (Short.MIN_VALUE + fill); 
		boolean flip = false;
		for(int y = 0; y < newImage.getHeight(); y++)
		for(int x = 0; x < newImage.getWidth(); x++) 
		{
			int newIndex = y * newImage.getWidthStep() + x * newImage.getNumChannels() * 2;
			
			short val = (cur++);
			if (flip)
				val ^= 0xFFFF;
			
	        newBuffer.putShort(newIndex, Short.reverseBytes(val));
	        
	        flip = !flip;
		}		
		
		return newImage;
	}
	
}
