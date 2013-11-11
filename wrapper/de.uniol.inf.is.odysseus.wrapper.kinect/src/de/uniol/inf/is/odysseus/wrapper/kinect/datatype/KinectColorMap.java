package de.uniol.inf.is.odysseus.wrapper.kinect.datatype;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.nio.ByteBuffer;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;

import de.uniol.inf.is.odysseus.wrapper.kinect.openNI.ContextHandler;

import static de.uniol.inf.is.odysseus.wrapper.kinect.utils.Constants.RED_PATTERN;
import static de.uniol.inf.is.odysseus.wrapper.kinect.utils.Constants.GREEN_PATTERN;
import static de.uniol.inf.is.odysseus.wrapper.kinect.utils.Constants.BLUE_PATTERN;

/**
 * A container data, that holds a color map in a byte array. It also provides some methods
 * to convert the data to a {@link BufferedImage} for use in AWT or {@link ImageData} for
 * use in SWT.
 * @author Juergen Boger <juergen.boger@offis.de>
 */
public class KinectColorMap {
    /** Depth count (RGB). */
    public static final int DEPTH = 3;

    /** Holds the color map bytes in RGB order. */
    private byte[] imgbytes;

    /** Width of the image. */
    private int width;

    /** Height of the image. */
    private int height;

    /**
     * Constructs a KinectColorMap from the given buffer. It operates from the given
     * buffer position and copies width * height * 3 bytes to a local byte array.
     * @param buffer
     * Buffer, that holds the color map data. The position should be on the first byte of
     * the color map.
     */
    public KinectColorMap(ByteBuffer buffer) {
        width = ContextHandler.getInstance().getWidth();
        height = ContextHandler.getInstance().getHeight();

        imgbytes = new byte[width * height * DEPTH];
        buffer.get(imgbytes);
    }
    
    public KinectColorMap(byte[] image, int width, int height) {
        this.width = width;
        this.height = height;

        imgbytes = image;
    }    

    /**
     * Copy Constructor.
     * @param copy
     * KinectColorMap to copy from.
     */
    public KinectColorMap(KinectColorMap copy) {
        this.width = copy.width;
        this.height = copy.height;
        this.imgbytes = new byte[copy.imgbytes.length];
        System.arraycopy(copy.imgbytes, 0, this.imgbytes, 0, imgbytes.length);
    }

    /**
     * Transforms the image by the given matrix. Not visible data will get black.
     * @param mat
     * A 2x3 transformation matrix.
     */
    public void affineTransform(double[][] mat) {
        byte[] res = new byte[imgbytes.length];
        int projX, projY, i, projI;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                i = (y * width + x) * DEPTH;

                projX = (int) (mat[0][0] * x + mat[0][1] * y + mat[0][2]);
                projY = (int) (mat[1][0] * x + mat[1][1] * y + mat[1][2]);

                if (projX < 0 || projX >= width || projY < 0 || projY >= height) {
                    res[i] = (byte) 0;
                    res[i + 1] = (byte) 0;
                    res[i + 2] = (byte) 0;
                    continue;
                }

                projI = (projY * width + projX) * DEPTH;
                res[i] = imgbytes[projI];
                res[i + 1] = imgbytes[projI + 1];
                res[i + 2] = imgbytes[projI + 2];
            }
        }
        this.imgbytes = res;
    }

    /**
     * Creates a {@link BufferedImage} from the color map to use in AWT.
     * @return A {@link BufferedImage}.
     */
    public BufferedImage getBufferedImage() {
        BufferedImage bimg = new BufferedImage(width, height,
                BufferedImage.TYPE_3BYTE_BGR);
        
        int i = 0;
        byte[] dstArray = ((DataBufferByte) bimg.getRaster().getDataBuffer())
                .getData();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                i = (y * width + x) * DEPTH;
                dstArray[i] = imgbytes[i + 2];
                dstArray[i + 1] = imgbytes[i + 1];
                dstArray[i + 2] = imgbytes[i];
            }
        }

        return bimg;
    }

    /**
     * Creates an {@link ImageData} object from the color map to use in SWT.
     * @return {@link ImageData}.
     */
    public ImageData getImageData() {
        PaletteData palette = new PaletteData(RED_PATTERN, GREEN_PATTERN,
                BLUE_PATTERN);
        ImageData imageData = new ImageData(width, height, Byte.SIZE * DEPTH,
                palette);
        System.arraycopy(imgbytes, 0, imageData.data, 0, imgbytes.length);

        return imageData;
    }

    /**
     * Returns the byte array.
     * @return Byte array.
     */
    public final byte[] getData() {
        return imgbytes;
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
