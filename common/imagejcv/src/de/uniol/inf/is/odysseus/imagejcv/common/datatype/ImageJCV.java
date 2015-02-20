package de.uniol.inf.is.odysseus.imagejcv.common.datatype;

import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_core.cvCreateImageHeader;
import static org.bytedeco.javacpp.opencv_core.cvReleaseImageHeader;
import static org.bytedeco.javacpp.opencv_core.cvSize;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;

import java.nio.ByteBuffer;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.opencv_core.IplImage;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.objecthandler.ObjectByteConverter;

/**
 * @author Kristian Bruns, Henrik Surm
 */

public class ImageJCV implements IClone, Cloneable 
{
	private int			width;
	private int			height;
	private int			numChannels;
	private int			depth;
	private int			widthStep;
	
	private IplImage 	image;
	private ByteBuffer 	imageData;

	public ByteBuffer getImageData()	{ return imageData; }
	public IplImage   getImage() 		{ return image; }
	
	public int 		getNumChannels()	{ return numChannels; }	
	public int 		getDepth()			{ return depth; }		
	public int 		getWidth() 			{ return width; }	
	public int 		getHeight() 		{ return height; }
	public int 		getWidthStep() 		{ return widthStep; }
	
	public ImageJCV(ImageJCV other) 
	{
		this(other.getWidth(), other.getHeight(), other.getDepth(), other.getNumChannels());
		image.getByteBuffer().put(other.image.getByteBuffer());
	}
	
	public ImageJCV(int width, int height)
	{
		this(width, height, IPL_DEPTH_8U, 4);
	}

	public ImageJCV(int width, int height, int depth, int channels)
	{
		// Save these values since each call to image.get... is a native call
		this.width = width;
		this.height = height;
		this.depth = depth;
		this.numChannels = channels;
		
		image = cvCreateImageHeader(cvSize(width, height), depth, channels);
		
		imageData = ByteBufferCache.getOrAllocateBuffer(image.imageSize());
		image.imageData(new BytePointer(imageData));
		
		widthStep = image.widthStep();
		
		ImageGC.onCreateImage();
	}		
	
	@Override public ImageJCV clone()
	{
		return new ImageJCV(this);
	}
	
	@Override
	protected void finalize()
	{
		ImageGC.onReleaseImage();
		release();
	}	
	
	public void release()
	{
		if (image != null)
		{
			cvReleaseImageHeader(image);
			image = null;		
			
			ByteBufferCache.putBuffer(imageData);
			imageData = null;
		}			
	}
	
	public int get(final int index) {
		ByteBuffer buffer = this.image.getByteBuffer();
		int value = buffer.getInt(index);
		return value;
	}
	
	public void set(int index, int value) {
		ByteBuffer buffer = this.image.getByteBuffer();
		buffer.putInt(index, value);
	}
	
	public void writeData(ByteBuffer buffer) {
		byte[] bytes = ObjectByteConverter.objectToBytes(image);
		buffer.put(bytes);
	}
	
	@Override
	public String toString() {
		return "{Width: " + getWidth() + " Height: " + getHeight() + "}";
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
			
			default: 
				throw new UnsupportedOperationException("toGrayscale not implemented for " + getNumChannels() + " channels!");
		}
		
		ImageJCV result = new ImageJCV(getWidth(), getHeight(), IPL_DEPTH_8U, 1);
		cvCvtColor(image, result.image, conversion);
		return result;
	}
	
	// toGrayscaleImage(false)
	public ImageJCV toGrayscaleImage()
	{
		return toGrayscaleImage(false);
	}

	
	public void fill(int value) {
		throw new UnsupportedOperationException("Currently not implemented");
	}
	
	public ImageJCV(ByteBuffer buffer) {
		throw new UnsupportedOperationException("Not implemented yet!");
/*		this.image = (IplImage) ObjectByteConverter.bytesToObject(buffer.array());
		ImageGC.onCreateImage();*/
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
}

class ByteBufferCache
{
	@SuppressWarnings("unused")
	private static long allocatedMemory = 0;
	private static Map<Integer, LinkedList<ByteBuffer>> cache = new HashMap<>();
	
	public synchronized static void putBuffer(ByteBuffer buffer)
	{
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
	private static final int startThreshold = 20;	// Only start thread when imageCount>startThreshold. Stop running thread when imageCount<=startThreshold. 
	private static final int gcThreshold = 200;		// Run System.gc() when imageCount>gcThreshold. Threshold should be a lot larger than the normal number of concurrent images in the system. 
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
