package de.uniol.inf.is.odysseus.imagejcv.common.datahandler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;

/**
 * DataHandler for Datatype ImageJCV.
 * 
 * @author Kristian Bruns, Henrik Surm
 */
public class ImageJCVDataHandler extends AbstractDataHandler<ImageJCV> {
	static protected List<String> types = new ArrayList<String>();
	static {
		ImageJCVDataHandler.types.add(SDFImageJCVDatatype.IMAGEJCV.getURI());
	}
	
	@Override public IDataHandler<ImageJCV> getInstance(final SDFSchema schema) 
	{
		return new ImageJCVDataHandler();
	}
	
	public ImageJCVDataHandler() 
	{
		super(null);
	}
	
	@Override public ImageJCV readData(final String string) 
	{
		return this.readData(ByteBuffer.wrap(string.getBytes()));
	}
	
	@Override public ImageJCV readData(InputStream inputStream) throws IOException 
	{
		return ImageJCV.fromStream(inputStream);
	}
	
	@Override public ImageJCV readData(final ByteBuffer buffer) 
	{
		return ImageJCV.fromBuffer(buffer);
	}
	
	@Override public void writeData(final ByteBuffer buffer, final Object data) 
	{
		ImageJCV image = (ImageJCV) data;
		image.appendToByteBuffer(buffer);
	}
	
	@Override final public List<String> getSupportedDataTypes() 
	{
		return ImageJCVDataHandler.types;
	}
	
	/**
	 * Returns memory size of an image.
	 */
	@Override public int memSize(final Object attribute) 
	{
		final ImageJCV image = (ImageJCV) attribute;
		return 4 * 4 + (int)Math.ceil(image.getWidth() * image.getHeight() * image.getNumChannels() * image.getDepth() / 8.0);
	}
	
	@Override public Class<?> createsType() 
	{
		return ImageJCV.class;
	}
}
