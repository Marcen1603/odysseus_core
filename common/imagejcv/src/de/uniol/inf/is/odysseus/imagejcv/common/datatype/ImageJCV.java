package de.uniol.inf.is.odysseus.imagejcv.common.datatype;

import static org.bytedeco.javacpp.avutil.AV_PIX_FMT_RGBA;
import static org.bytedeco.javacpp.opencv_core.*;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.imagejcv.common.cache.ImageCacheProvider;
import de.uniol.inf.is.odysseus.imagejcv.common.cache.ImageGarbageCollector;

abstract class GCMonitored
{
	public GCMonitored()
	{
		ImageGarbageCollector.onCreateImage();
	}

	protected abstract void releaseImage();
	
	@Override protected void finalize() throws Throwable
	{
		releaseImage();
		super.finalize();
	}
}

public class ImageJCV extends GCMonitored implements IClone, Cloneable 
{
	// TODO: Löschen, nur als kurzfristiger Hack zur synchrnoisation von Basler und Optris benutzt
	public static long startTime;
	
	private IplImage image;
	
	private int	width;
	private int	height;
	private int	numChannels;
	private int	depth;
	private int	widthStep;
	private int	pixelFormat;	
	
	public int getNumChannels()	{ return numChannels; }	
	public int getDepth() { return depth; }		
	public int getWidth() { return width; }	
	public int getHeight() { return height; }
	public int getWidthStep() { return widthStep; }
	public int getPixelFormat()	{ return pixelFormat; }	
	public ByteBuffer getImageData() { return image.imageData().position(0).limit(image.imageSize()).asByteBuffer(); }
	public IplImage getImage() { return image; }
	
	private ImageJCV() {}
	
	public ImageJCV(ImageJCV other) 
	{
		this(other.getWidth(), other.getHeight(), other.getDepth(), other.getNumChannels(), other.getPixelFormat());
		getImageData().put(other.getImageData());
	}
	
	public ImageJCV(int width, int height)
	{
		this(width, height, IPL_DEPTH_8U, 4, AV_PIX_FMT_RGBA);
	}

	public ImageJCV(int width, int height, int depth, int channels, int pixelFormat)
	{
		// Save these values since each call to image.get... is a native call
		this.width = width;
		this.height = height;
		this.depth = depth;
		this.numChannels = channels;
		this.pixelFormat = pixelFormat;
		
		image = ImageCacheProvider.getInstance().getImage(width, height, depth, channels); // IplImage.create(cvSize(width, height), depth, channels);
		widthStep = image.widthStep();
	}		
	
	// Creates an image which has the input iplImage as underlying buffer.
	// The input image will be consumed and later be added to the image cache, so no references to this iplImage should be kept outside! 
	public static ImageJCV wrap(IplImage iplImage, int pixelFormat)
	{
		ImageJCV myImage = new ImageJCV();
		myImage.image = iplImage;
		myImage.pixelFormat = pixelFormat;
		
		myImage.width = iplImage.width();
		myImage.height = iplImage.height();
		myImage.depth = iplImage.depth();
		myImage.numChannels = iplImage.nChannels();
		myImage.widthStep = iplImage.widthStep();		
		
		return myImage;
	}
	
	// Creates an image with same parameters as the other image
	public static ImageJCV createCompatible(ImageJCV other)
	{
		return new ImageJCV(other.getWidth(), other.getHeight(), other.getDepth(), other.getNumChannels(), other.getPixelFormat());	
	}	
	
    // Copies content of an IplImage into a new image
	public static ImageJCV fromIplImage(IplImage iplImage, int pixelFormat) 
	{
		ImageJCV result = new ImageJCV(iplImage.width(), iplImage.height(), iplImage.depth(), iplImage.nChannels(), pixelFormat);
		result.getImageData().put(iplImage.imageData().position(0).limit(iplImage.imageSize()).asByteBuffer());
		return result;
	}
    
	// Copies contents of buffered image into a new ImageJCV
	public static ImageJCV fromBufferedImage(BufferedImage bufferedImage)
	{
		// TODO: Convert bufferedImage pixelformat to OpenCV pixelformat		
		return ImageJCV.fromIplImage(new OpenCVFrameConverter.ToIplImage().convert(new Java2DFrameConverter().convert(bufferedImage)), AV_PIX_FMT_RGBA);
	}
	
	// Copies content of a Frame into a new image
	public static ImageJCV fromFrame(Frame frame, int pixelFormat) 
	{
		if (frame == null) return null;
		
		IplImage iplImage;
		if (frame.opaque instanceof IplImage) iplImage = (IplImage) frame.opaque;
		
        int depth = OpenCVFrameConverter.getIplImageDepth(frame.imageDepth);
        iplImage = depth == -1 ? null : IplImage.createHeader(frame.imageWidth, frame.imageHeight, depth, frame.imageChannels)
        		.imageData(new BytePointer(new Pointer(frame.image[0].position(0))))
                .widthStep(frame.imageStride * Math.abs(frame.imageDepth) / 8)
                .imageSize(frame.image[0].capacity() * Math.abs(frame.imageDepth) / 8);		
		
        return iplImage == null ? null : ImageJCV.fromIplImage(iplImage, pixelFormat);
	}	
	
	// Reads an image from a stream
	public static ImageJCV fromStream(InputStream inputStream) throws IOException 
	{
		DataInputStream stream = new DataInputStream(inputStream);
		ImageJCV result = new ImageJCV(stream.readInt(), stream.readInt(), stream.readInt(), stream.readInt(),  stream.readInt());
		ByteBuffer newBuffer = result.getImageData();

		while (newBuffer.remaining() > 0)
		{
			int data = stream.read();
			if (data == -1) 
				throw new IOException("End of stream reached before image was read completely");
				
			newBuffer.put((byte)data);
		}

		return result;
	}
	
	// Reads an image from a buffer
	public static ImageJCV fromBuffer(ByteBuffer buffer) 
	{
		ImageJCV result = new ImageJCV(buffer.getInt(), buffer.getInt(), buffer.getInt(), buffer.getInt(), buffer.getInt());
		int imageSize = result.image.imageSize();
		
		ByteBuffer newBuffer = result.getImageData();
		newBuffer.put((ByteBuffer) buffer.slice().limit(imageSize));
		buffer.position(buffer.position() + imageSize);
		
		return result;
	}	
	
	// Writes this image to a byte buffer
	public void appendToByteBuffer(ByteBuffer buffer) 
	{
		buffer.putInt(width);
		buffer.putInt(height);
		buffer.putInt(depth);
		buffer.putInt(numChannels);
		buffer.putInt(pixelFormat);
		
		buffer.put(getImageData());
	}

	// Creates a byte buffer and writes this image to it
	public ByteBuffer toByteBuffer() 
	{
		ByteBuffer buffer = ByteBuffer.allocate(5*4 + getImage().imageSize());
		buffer.putInt(width);
		buffer.putInt(height);
		buffer.putInt(depth);
		buffer.putInt(numChannels);
		buffer.putInt(pixelFormat);
		
		buffer.put(getImageData());
		return buffer;
	}
	
	
	// Returns a copy of this image
	@Override public ImageJCV clone()
	{
		return new ImageJCV(this);
	}
	
	@Override public String toString() {
		return "{Width: " + getWidth() + " Height: " + getHeight() + " Depth: " + getDepth() + " Channels: " + getNumChannels() + "}";
	}	
	
	@Override
	public void releaseImage() 
	{
		if (image != null)
		{
			ImageCacheProvider.getInstance().putImage(image);
			image = null;			
		}
	}
}
