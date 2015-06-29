package de.uniol.inf.is.odysseus.image.functions;

import java.awt.image.BufferedImage;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.collection.ByteBufferWrapper;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.image.common.datatype.Image;
import de.uniol.inf.is.odysseus.image.common.sdf.schema.SDFImageDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class ToImageFromBufferFunction extends AbstractFunction<Image> 
{
	private static final long serialVersionUID = 282271378681582656L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {SDFDatatype.NUMBERS, SDFDatatype.NUMBERS, {SDFDatatype.BYTEBUFFER}};
	
	public ToImageFromBufferFunction() {
		super("toImage", 3, ToImageFromBufferFunction.ACC_TYPES, SDFImageDatatype.IMAGE);
	}
	
	// Creates an image with width and height given by the input values 0 and 1 and input given by input value 2 as a 32bpp bytebuffer.
	@Override public Image getValue() 
	{
		int width = getNumericalInputValue(0).intValue();
		int height = getNumericalInputValue(1).intValue();
		ByteBufferWrapper byteBuffer = (ByteBufferWrapper) getInputValue(2);
		
		Preconditions.checkArgument(width > 0, "Invalid Dimension");
		Preconditions.checkArgument(height > 0, "Invalid Dimension");
		
		// TODO: Optimize implementation
		// TODO: Create implementation for ImageJCV
		Image image = new Image(width, height);
		BufferedImage bufImg = image.getImage();
		
		for (int y=0; y<2048; y++)
		for (int x=0; x<2048; x++)
		{
			bufImg.setRGB(x, y, byteBuffer.getBuffer().getInt());
		}

		return image;
	}
	
}
