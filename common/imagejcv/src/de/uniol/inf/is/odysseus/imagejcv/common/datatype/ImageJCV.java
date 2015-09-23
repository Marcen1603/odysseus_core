package de.uniol.inf.is.odysseus.imagejcv.common.datatype;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.avutil.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;

import de.uniol.inf.is.odysseus.core.IClone;

/**
 * @author Kristian Bruns, Henrik Surm
 */

public class ImageJCV implements IClone, Cloneable 
{
	public static IplImage current = null;
	
	private byte[]		garbage;
	private int			width;
	private int			height;
	private int			numChannels;
	private int			depth;
	private int			widthStep;
	private int			pixelFormat;
	
	private IplImage 	image;
	private ByteBuffer 	imageData;

	public ByteBuffer getImageData()	{ return (ByteBuffer) imageData.position(0); }
	
	public IplImage   getImage() 		{ return image; }
	
	public int 		getNumChannels()	{ return numChannels; }	
	public int 		getDepth()			{ return depth; }		
	public int 		getWidth() 			{ return width; }	
	public int 		getHeight() 		{ return height; }
	public int 		getWidthStep() 		{ return widthStep; }
	public int		getPixelFormat()	{ return pixelFormat; }
	
	public ImageJCV(ImageJCV other) 
	{
		this(other.getWidth(), other.getHeight(), other.getDepth(), other.getNumChannels(), other.getPixelFormat());
		other.imageData.rewind();
		imageData.put(other.imageData);
	}
	
	public ImageJCV(int width, int height)
	{
		this(width, height, IPL_DEPTH_8U, 4, AV_PIX_FMT_RGBA);
	}

	public ImageJCV(int width, int height, int depth, int channels, int pixelFormat)
	{
		// Create a dummy payload which triggers the gc early
		// Native memory is not watched by the gc and each image only holds around 24bytes of java heap space.
		// A 4k RGBA image holds around 16MB native memory and we would run out of memory very fast since 
		// the gc will not be triggered before several thousand images have been generated.
		// The payload increases the java heap footprint by around 10% of the image size, so the gc runs earlier.
		garbage = new byte[width*height/4];
		garbage[0] = 0;
		garbage[1] = (byte)(Math.random()*255.0);
		
		// Save these values since each call to image.get... is a native call
		this.width = width;
		this.height = height;
		this.depth = depth;
		this.numChannels = channels;
		this.pixelFormat = pixelFormat;
		
		image = IplImage.createHeader(cvSize(width, height), depth, channels);
		
		imageData = ByteBufferCache.getOrAllocateBuffer(image.imageSize());
		image.imageData(new BytePointer(imageData));
		
		widthStep = image.widthStep();
		
//		ImageGC.onCreateImage();
	}		
	
	public static ImageJCV createCompatible(ImageJCV other)
	{
		return new ImageJCV(other.getWidth(), other.getHeight(), other.getDepth(), other.getNumChannels(), other.getPixelFormat());	
	}
	
	@Override public ImageJCV clone()
	{
		return new ImageJCV(this);
	}
	
/*	@Override
	protected void finalize()
	{
		ImageGC.onReleaseImage();
//		release();
	}	*/
	
	public void release()
	{
		if (image != null)
		{
//			System.out.println("Release enter");
			
			ByteBufferCache.putBuffer(imageData);
			imageData = null;
			image.imageData(null);			
			image = null;		
			
//			System.out.println("Release leave");
		}			
	}
	
	public int get(final int index) 
	{
		int value = imageData.getInt(index);
		return value;
	}
	
	public void set(int index, int value) 
	{
		imageData.putInt(index, value);
	}
	
	public void writeData(ByteBuffer buffer) 
	{
		buffer.putInt(width);
		buffer.putInt(height);
		buffer.putInt(depth);
		buffer.putInt(numChannels);
		buffer.putInt(pixelFormat);
		
		imageData.rewind();
		buffer.put(imageData);
	}
	
	@Override
	public String toString() {
		return "{Width: " + getWidth() + " Height: " + (getHeight() + garbage[0]) + "}";
	}

	// Returns this image converted to grayscale. Returns this, when this image already is a grayscale image and doClone = false
	public ImageJCV toGrayscaleImage(boolean doClone)
	{
		int conversion = -1;
		switch (getNumChannels())
		{
			case 1:
				return doClone ? clone() : this;
				
			case 3:
				conversion = CV_BGR2GRAY; 				
				break;
				
			case 4:
				conversion = CV_BGRA2GRAY;
				break;
			
			default: 
				throw new UnsupportedOperationException("toGrayscale not implemented for " + getNumChannels() + " channels!");
		}
		
		ImageJCV result = new ImageJCV(getWidth(), getHeight(), IPL_DEPTH_8U, 1, AV_PIX_FMT_GRAY8);
		cvCvtColor(image, result.image, conversion);
		return result;
	}
	
	// toGrayscaleImage(false)
	public ImageJCV toGrayscaleImage()
	{
		return toGrayscaleImage(false);
	}
	
	public static ImageJCV extendToMultipleOf(ImageJCV image, int multiple)
	{
		int newWidth  = image.getWidth();
		int newHeight = image.getHeight(); 
		
		if (newWidth  % multiple != 0) newWidth  = (newWidth / multiple + 1) * multiple;
		if (newHeight % multiple != 0) newHeight = (newHeight / multiple + 1) * multiple;
		
		if (newWidth == image.getWidth() && newHeight == image.getHeight()) return image;
		
		ImageJCV newImage = new ImageJCV(newWidth, newHeight, image.getDepth(), image.getNumChannels(), image.getPixelFormat());
		int bytesPerPixel = Math.abs(newImage.getDepth() & 0xFF) / 8 * newImage.getNumChannels();
		ByteBuffer newBuffer = newImage.getImageData();
		ByteBuffer oldBuffer = image.getImageData();
		
		newBuffer.rewind();
		oldBuffer.rewind();
		
		if (newWidth == image.getWidth())
		{
			// Only some lines have been added to the end. 
			// Since width and widthStep is the same, the whole memory block of the old image can be copied						 
			newBuffer.put(oldBuffer);												
		}
		else
		{
			// Since the width has changed, each line has to be copied seperately
			for (int y=0;y<image.getHeight();y++)
			{
				int oldLineStart = y*image.getWidthStep();
				int newLineStart = y*newImage.getWidthStep();
				
				oldBuffer.position(oldLineStart);
				newBuffer.position(newLineStart);
				
				// Copy whole line
				for (int x = 0; x < image.getWidth()*bytesPerPixel; x++)
					newBuffer.put(oldBuffer.get());
				
				// Fill remaining empty pixel
				int remaining = newWidth - image.getWidth();
				for (int x = 0; x < remaining * bytesPerPixel; x++)
					newBuffer.put((byte)0);
			}
		}
		
		// If new lines have been added at the bottom, fill them with 0
		if (newHeight != image.getHeight())
		{
			for (int y=image.getHeight(); y<newHeight; y++)
			{
				newBuffer.position(y*newImage.getWidthStep());
				for (int x = 0; x < newWidth*bytesPerPixel; x++)
					newBuffer.put((byte)0);
			}
		}		
		
		return newImage;
	}		
	
	public void fill(int value) {
		throw new UnsupportedOperationException("Currently not implemented");
	}
		
	public ImageJCV(double[][] data) 
	{
		throw new UnsupportedOperationException("Noch nicht überarbeitet!");
/*		image = cvCreateImage(cvSize(data[0].length, data.length), IPL_DEPTH_8U, 4);
		ByteBuffer buffer = image.getByteBuffer();
		for (int i=0; i < data.length; i++) {
			buffer.putDouble(i, data[i][0]);
		}
		ImageGC.onCreateImage();*/
	}
	
	public double[][] getMatrix() {
		throw new UnsupportedOperationException("Currenlty not implemented");
	}
	
	public void copyFrom(IplImage iplImage) 
	{
		// TODO: Make sure image fits!
		imageData.rewind();
		
		if (widthStep != iplImage.widthStep())
		{
			throw new UnsupportedOperationException("Needs to be reimplemented!");
//			image.copyFrom(iplImage.getBufferedImage());						
		}
		else
		{
			imageData.put(iplImage.imageData().position(0).limit(iplImage.imageSize()).asByteBuffer());
		}
	}
		
	private static Java2DFrameConverter bufferedImageConverter = null;
	private static OpenCVFrameConverter.ToIplImage iplImageConverter = null;
	
	private static Java2DFrameConverter getBufferedImageConverter()
	{
		if (bufferedImageConverter == null)
			bufferedImageConverter = new Java2DFrameConverter();
		
		return bufferedImageConverter;
	}

	private static OpenCVFrameConverter.ToIplImage getIplImageConverter()
	{
		if (iplImageConverter == null)
			iplImageConverter = new OpenCVFrameConverter.ToIplImage();
		
		return iplImageConverter;
	}
	
	
	public static ImageJCV fromBufferedImage(BufferedImage bufferedImage)
	{
		// TODO: Convert bufferedImage pixelformat to OpenCV pixelformat
		
		return ImageJCV.fromIplImage(getIplImageConverter().convert(getBufferedImageConverter().convert(bufferedImage)), AV_PIX_FMT_RGBA);
	}
	
	public static ImageJCV fromIplImage(IplImage iplImage, int pixelFormat)
	{
		ImageJCV image = new ImageJCV(iplImage.width(), iplImage.height(), iplImage.depth(), iplImage.nChannels(), pixelFormat);
		image.getImageData().rewind();
		image.getImageData().put(iplImage.imageData().position(0).limit(iplImage.imageSize()).asByteBuffer());																	
		
		return image;
	}
	
	public static ImageJCV fromStream(InputStream inputStream) throws IOException 
	{
		throw new UnsupportedOperationException("Not implemented yet!");
		
/*		DataInputStream stream = new DataInputStream(inputStream);
		ImageJCV result = new ImageJCV(stream.readInt(), stream.readInt(), stream.readInt(), stream.readInt());

		// Doesn't work for direct byte buffers
		stream.read(result.getImageData().array(), 16, result.getImage().imageSize());

		return result;*/
	}
	
	public static ImageJCV fromBuffer(ByteBuffer buffer) 
	{
		ImageJCV result = new ImageJCV(buffer.getInt(), buffer.getInt(), buffer.getInt(), buffer.getInt(), buffer.getInt());
		
		buffer.position(buffer.limit());
		
//		result.getImageData().rewind();
		
		int size = 0; //result.getImage().imageSize();
		for (int i=0;i<size;i++)
		{
			result.getImageData().put((byte) 0xFF);
		}
		
//		result.getImageData().position(result.getImageData().limit());
		
/*		int oldLimit = buffer.limit();
		buffer.limit(buffer.position() + result.getImage().imageSize());
		result.getImageData().rewind();
		result.getImageData().put(buffer);
		buffer.limit(oldLimit);*/
		
		return result;
	}	
}

class ByteBufferCache
{
	@SuppressWarnings("unused")
	private static long allocatedMemory = 0;
	private static Map<Integer, LinkedList<ByteBuffer>> cache = new HashMap<>();
	
	public synchronized static void putBuffer(ByteBuffer buffer)
	{
		if (1==1) return;
		
		if (!buffer.isDirect())
			throw new InvalidParameterException("buffer must be a direct ByteBuffer!");

		LinkedList<ByteBuffer> list = cache.get(buffer.capacity());
		
		if (list != null)
		{
			if (list.size() < 100)
			{
				list.add(buffer);
//				System.out.println("Buffer added to cache, num buffers = " + list.size());
			}
			else
			{
//				System.out.println("Cache full, buffer dismissed");
				allocatedMemory -= buffer.capacity();
			}
		}
		else
		{
			list = new LinkedList<ByteBuffer>();
			list.add(buffer);
			cache.put(buffer.capacity(), list);
		}		
	}
	
	public synchronized static ByteBuffer getOrAllocateBuffer(int capacity)
	{
		if (1==1)
			return ByteBuffer.allocateDirect(capacity);
		
		LinkedList<ByteBuffer> list = cache.get(capacity);
		
		if (list == null || list.isEmpty())
		{
			allocatedMemory += capacity;
//			System.out.println("New buffer created! Total mem = " + allocatedMemory);
			
			return ByteBuffer.allocateDirect(capacity);
		}
		else
		{
//			System.out.println("Buffer recycled! " + (list.size()-1) + " Buffers left");
			return (ByteBuffer) list.removeFirst();
		}
	}
}

// This static class monitors the number of un-finalized ImageJCV objects. Since native memory allocation will not trigger
// GC, it has to be done manually. When there are enough un-finalized objects, a thread is started, which monitors the image 
// count and calls System.gc() when a threshold is reached. If there are only a few objects left, the thread finishes on its own.
// To monitor the number of images, onCreateImage and onReleaseImage have to be called in constructor/finalize methods.
//
// NOTE: If the GC is triggered by normal java heap allocations, native code objects destroyed by java finalizers will also be collected.
//       If there is enough "other" memory activity, this class may not be needed.
//
// TODO: Implement a DirectByteBuffer cache or an IplImage cache, from which old images can be reused? This might be feasible, since
//       images from the same camera are always the same size (their buffers too)
class ImageGC
{
	private static final int startThreshold = 20; //20;	// Only start thread when imageCount>startThreshold. Stop running thread when imageCount<=startThreshold. 
	private static final int gcThreshold = 200; //200;		// Run System.gc() when imageCount>gcThreshold. Threshold should be a lot larger than the normal number of concurrent images in the system. 
	private static final int checkInterval = 500;	// Sleep for checkInterval in each iteration
	
	private static Thread gcThread = null;
	private static AtomicInteger imageCount = new AtomicInteger(0);
	
	public static void onCreateImage()
	{		
		int iCount = imageCount.incrementAndGet();
		
		if (gcThread == null && iCount > startThreshold)
		{			
			gcThread = 	new Thread()
						{
							@Override public void run()
							{
								System.out.println(Thread.currentThread().getName() + " started");
								
								while (imageCount.get() > startThreshold)
								{
									try 
									{
										int iCount = imageCount.get();
										if (iCount > gcThreshold)
										{	
											long start = System.nanoTime();
											System.gc();
											System.out.println("System.gc time = " + (System.nanoTime() - start) / 1.0e6 + " ms. ImageCount before gc = " + iCount);
										} 
										Thread.sleep(checkInterval);
									}
									catch (InterruptedException e) 
									{
										e.printStackTrace();
									}											
								}

								gcThread = null;
								System.out.println(Thread.currentThread().getName() + " stopped");
							}
						};
			gcThread.setPriority(Thread.MIN_PRIORITY);
			gcThread.setName("ImageJCV native memory GC thread");
			gcThread.start();
		}
	}

	public static void onReleaseImage() 
	{
		imageCount.decrementAndGet();
	}
}
