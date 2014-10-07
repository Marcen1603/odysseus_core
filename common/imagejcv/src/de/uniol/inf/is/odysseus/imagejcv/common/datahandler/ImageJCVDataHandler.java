package de.uniol.inf.is.odysseus.imagejcv.common.datahandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
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
 * @author Kristian Bruns
 */
public class ImageJCVDataHandler extends AbstractDataHandler<ImageJCV> {
	static protected List<String> types = new ArrayList<String>();
	static {
		ImageJCVDataHandler.types.add(SDFImageJCVDatatype.IMAGEJCV.getURI());
	}
	
	@Override
	public IDataHandler<ImageJCV> getInstance(final SDFSchema schema) {
		return new ImageJCVDataHandler();
	}
	
	public ImageJCVDataHandler() {
		super();
	}
	
	@Override
	public ImageJCV readData(final ObjectInputStream inputStream) throws IOException {
		IplImage image = null;
		try {
			image = (IplImage) inputStream.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (image != null) {
			return new ImageJCV(image);
		} else {
			return null;
		}
	}
	
	@Override
	public ImageJCV readData(final String string) {
		return this.readData(ByteBuffer.wrap(string.getBytes()));
	}
	
	@Override
	public ImageJCV readData(InputStream inputStream) throws IOException {
		IplImage image = IplImage.createFrom(ImageIO.read(inputStream));
		return new ImageJCV(image);
	}
	
	@Override
	public ImageJCV readData(final ByteBuffer buffer) {
		return new ImageJCV(buffer);
	}
	
	@Override
	public void writeData(final ByteBuffer buffer, final Object data) {
		ImageJCV image = (ImageJCV) data;
		image.writeData(buffer);
	}
	
	@Override
	final public List<String> getSupportedDataTypes() {
		return ImageJCVDataHandler.types;
	}
	
	@Override
	public int memSize(final Object attribute) {
		final ImageJCV image = (ImageJCV) attribute;
		// TODO: Has this been updates since the change to IplImage?
		return (2* Integer.SIZE + image.getWidth() * image.getHeight() * Double.SIZE) / 8;
	}
	
	@Override
	public Class<?> createsType() {
		return ImageJCV.class;
	}
}
