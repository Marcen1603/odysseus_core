package de.uniol.inf.is.odysseus.imagejcv.common.datahandler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;

import org.bytedeco.javacpp.opencv_core.IplImage;

/**
 * DataHandler for Datatype ImageJCV.
 * 
 * @author Kristian Bruns
 */
public class ImageJCVDataHandler extends AbstractDataHandler<ImageJCV> {
	static protected List<String> types = new ArrayList<String>();
	static {
		ImageJCVDataHandler.types.add(SDFImageJCVDatatype.IMAGEJCV.getURI());
	}
	
	/**
	 * Creates an Instance of ImageJCVDataHandler.
	 * 
	 * @author Kristian Bruns
	 * 
	 * @return IDataHandler<ImageJCV> Instance of ImageJCVDataHandler.
	 */
	@Override
	public IDataHandler<ImageJCV> getInstance(final SDFSchema schema) {
		return new ImageJCVDataHandler();
	}
	
	public ImageJCVDataHandler() {
		super(null);
	}
	
	
	/**
	 * Reads data from String.
	 * 
	 * @author Kristian Bruns
	 * 
	 * @return ImageJCV Read image.
	 */
	@Override
	public ImageJCV readData(final String string) {
		return this.readData(ByteBuffer.wrap(string.getBytes()));
	}
	
	/**
	 * Reads data from InputStream. 
	 * 
	 * @author Kristian Bruns
	 * 
	 * @return ImageJCV Read image.
	 */
	@Override
	public ImageJCV readData(InputStream inputStream) throws IOException 
	{
		throw new UnsupportedOperationException("Not implemented yet!");
/*		IplImage image = IplImage.createFrom(ImageIO.read(inputStream));
		return new ImageJCV(image);*/
	}
	
	/**
	 * Reads data from ByteBuffer.
	 * 
	 * @author Kristian Bruns
	 * 
	 * @return ImageJCV Read image.
	 */
	@Override
	public ImageJCV readData(final ByteBuffer buffer) {
		return new ImageJCV(buffer);
	}
	
	/**
	 * Writes data to image.
	 * 
	 * @author Kristian Bruns
	 * 
	 * @return void
	 */
	@Override
	public void writeData(final ByteBuffer buffer, final Object data) {
		ImageJCV image = (ImageJCV) data;
		image.writeData(buffer);
	}
	
	/**
	 * Returns supported data types.
	 * 
	 * @author Kristian Bruns
	 * 
	 * @return List<String> Supported data types.
	 */
	@Override
	final public List<String> getSupportedDataTypes() {
		return ImageJCVDataHandler.types;
	}
	
	/**
	 * Returns memory size of an image.
	 * 
	 * @author Kristian Bruns
	 * 
	 * @return int Memory Size.
	 */
	@Override
	public int memSize(final Object attribute) 
	{
		final ImageJCV image = (ImageJCV) attribute;
		
		// TODO: Has this been updates since the change to IplImage?
		return (2* Integer.SIZE + image.getWidth() * image.getHeight() * Double.SIZE) / 8;
	}
	
	/**
	 * Creates type ImageJCV.
	 * 
	 * @author Kristian Bruns
	 * 
	 * @return Class<?> ImageJCV.
	 */
	@Override
	public Class<?> createsType() {
		return ImageJCV.class;
	}
}
