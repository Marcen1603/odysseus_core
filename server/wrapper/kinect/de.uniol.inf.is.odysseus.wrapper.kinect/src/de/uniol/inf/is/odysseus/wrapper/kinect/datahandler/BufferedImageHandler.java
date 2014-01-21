package de.uniol.inf.is.odysseus.wrapper.kinect.datahandler;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.wrapper.kinect.sdf.schema.SDFKinectDatatype;

public class BufferedImageHandler extends AbstractDataHandler<BufferedImage> {
    /** Static field containing the supported types. */
    private static List<String> types = new ArrayList<String>();
    static {
        types.add(SDFKinectDatatype.BUFFERED_IMAGE.getURI());
    }

    /**
     * Standard constructor.
     */
    public BufferedImageHandler() {
    }

    /**
     * Constructor with schema.
     * @param schema
     * Passed schema.
     */
    public BufferedImageHandler(SDFSchema schema) {
    }

	@Override
	public BufferedImage readData(ByteBuffer buffer) {
		try {
			byte[] data = new byte[buffer.remaining()];
			buffer.get(data);
			// convert byte array back to BufferedImage
			InputStream in = new ByteArrayInputStream(data);
			BufferedImage bImageFromConvert = ImageIO.read(in);
			return bImageFromConvert;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public BufferedImage readData(ObjectInputStream inputStream)
			throws IOException {
		try {			
			BufferedImage rect = (BufferedImage) inputStream.readObject();
			return rect;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public BufferedImage readData(String string) {
        throw new RuntimeException("Method is not implemented.");
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {

		try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
	        BufferedImage img = (BufferedImage) data;
			
			ImageIO.write(img, "png", baos);
			baos.flush();
			byte[] bytes = baos.toByteArray();
			buffer.put(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int memSize(Object attribute) {
		return 0;
	}

	@Override
	public Class<?> createsType() {
		return List.class;
	}

	@Override
	protected IDataHandler<BufferedImage> getInstance(SDFSchema schema) {
		return new BufferedImageHandler(schema);
	}

	@Override
	public List<String> getSupportedDataTypes() {
		return Collections.unmodifiableList(types);
	}

}
