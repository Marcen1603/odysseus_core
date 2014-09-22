package de.uniol.inf.is.odysseus.wrapper.kinect.datatype;

import static de.uniol.inf.is.odysseus.wrapper.kinect.utils.Constants.BLUE_PATTERN;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.nio.ByteBuffer;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;

import de.uniol.inf.is.odysseus.wrapper.kinect.openNI.ContextHandler;

/**
 * A container data, that holds a depth map in a short array. Each value is the range from
 * the depth sensor to the object in mm. It also provides some methods to convert the data
 * to a {@link BufferedImage} for use in AWT or {@link ImageData} for use in SWT.
 * @author Juergen Boger <juergen.boger@offis.de>
 */
public class KinectDepthMap {
    /** Array storing the depth data. */
    private short[] imgbytes;
    
    /** Width of the image. */
    private int width;
    
    /** Height of the image. */
    private int height;
    
    /**
     * Constructs a KinectDepthMap from the given buffer. It operates from the given
     * buffer position and copies width * height * 2 bytes to a local short array.
     * @param buffer
     * Buffer, that holds the depth map data. The position should be on the first byte of
     * the depth map.
     */
    public KinectDepthMap(ByteBuffer buffer) {
        width = ContextHandler.getInstance().getWidth();
        height = ContextHandler.getInstance().getHeight();

        byte[] temp = new byte[width * height * 2];
        imgbytes = new short[width * height];
        buffer.get(temp);

        ByteBuffer tempBuffer = ByteBuffer.wrap(temp);
        tempBuffer.asShortBuffer().get(imgbytes);
    }
    
    public KinectDepthMap(short[] image, int width, int height) {
        this.width = width;
        this.height = height;
        imgbytes = image;
	}

	/**
     * Creates a {@link BufferedImage} from the depth map to use in AWT.
     * @return A {@link BufferedImage}.
     */
    public BufferedImage getBufferedImage() {
        BufferedImage bimg = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        byte[] dstArray = ((DataBufferByte) bimg.getRaster().getDataBuffer())
                .getData();
        
        short max = Short.MIN_VALUE;
        int i = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                short len = imgbytes[i++];
                if (len > max) {
                    max = len;
                }
            }
        }
        i = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                dstArray[i] = (byte) (((float) imgbytes[i] / (float) max) * BLUE_PATTERN);
                i++;
            }
        }
        
        return bimg;
    }

    /**
     * Returns the short array.
     * @return Short array.
     */
    public short[] getData() {
        return imgbytes;
    }

    /**
     * Creates an {@link ImageData} object from the depth map to use in SWT.
     * @return {@link ImageData}.
     */
    public ImageData getImageData() {
        PaletteData palette = new PaletteData(BLUE_PATTERN, BLUE_PATTERN,
                BLUE_PATTERN);
        ImageData imageData = new ImageData(width, height, Byte.SIZE, palette);

        short max = Short.MIN_VALUE;
        int i = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                short len = imgbytes[i++];
                if (len > max) {
                    max = len;
                }
            }
        }
        i = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int len = (int) (((float) imgbytes[i] / (float) max) * BLUE_PATTERN);
                imageData.data[i] = (byte) len;
                i++;
            }
        }

        return imageData;
    }
    
    /**
     * Gets the width of the image.
     * @return
     * Width.
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * Gets the height of the image.
     * @return
     * Height.
     */
    public int getHeight() {
        return height;
    }
}
