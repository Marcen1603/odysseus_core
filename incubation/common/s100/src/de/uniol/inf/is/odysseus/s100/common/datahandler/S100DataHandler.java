package de.uniol.inf.is.odysseus.s100.common.datahandler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.s100.common.FeatureType;
import de.uniol.inf.is.odysseus.s100.common.sdf.schema.SDFS100DataType;

/**
 * DataHandler for Datatype ImageJCV.
 * 
 * @author Kristian Bruns
 */
public class S100DataHandler extends AbstractDataHandler<FeatureType> {
	static protected List<String> types = new ArrayList<String>();
	static {
		S100DataHandler.types.add(SDFS100DataType.S100.getURI());
	}
	
	/**
	 * Creates an Instance of ImageJCVDataHandler.
	 * 
	 * @author Kristian Bruns
	 * 
	 * @return IDataHandler<ImageJCV> Instance of ImageJCVDataHandler.
	 */
	@Override
	public IDataHandler<FeatureType> getInstance(final SDFSchema schema) {
		return new S100DataHandler();
	}
	
	public S100DataHandler() {
		super(null);
	}
	
	
	/**
	 * Reads data from String.
	 * 
	 * @author Kristian Bruns
	 * 
	 * @return ImageJCV Readed Image.
	 */
	@Override
	public FeatureType readData(final String string) 
	{
		return null;//this.readData(ByteBuffer.wrap(string.getBytes()));
	}
	
	/**
	 * Reads data from InputStream. 
	 * 
	 * @author Kristian Bruns
	 * 
	 * @return ImageJCV Readed Image.
	 */
	@Override
	public FeatureType readData(InputStream inputStream) throws IOException 
	{
//		IplImage image = IplImage.createFrom(ImageIO.read(inputStream));
		return null; //new ImageJCV(image);
	}
	
	/**
	 * Reads data from ByteBuffer.
	 * 
	 * @author Kristian Bruns
	 * 
	 * @return ImageJCV Readed Image.
	 */
	@Override
	public FeatureType readData(final ByteBuffer buffer) {
		return null; //new ImageJCV(buffer);
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
//		ImageJCV image = (ImageJCV) data;
//		image.writeData(buffer);
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
		return S100DataHandler.types;
	}
	
	/**
	 * Returns memory size of an image.
	 * 
	 * @author Kristian Bruns
	 * 
	 * @return int Memory Size.
	 */
	@Override
	public int memSize(final Object attribute) {
//		final ImageJCV image = (ImageJCV) attribute;
		// TODO: Has this been updates since the change to IplImage?
		return 0; //(2* Integer.SIZE + image.getWidth() * image.getHeight() * Double.SIZE) / 8;
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
		return FeatureType.class;
	}
}
