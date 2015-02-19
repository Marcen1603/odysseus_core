package de.uniol.inf.is.odysseus.imagejcv.common.datatype;

import static org.bytedeco.javacpp.opencv_core.*;

import org.bytedeco.javacpp.opencv_core.IplImage;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.objecthandler.ObjectByteConverter;

/**
 * @author Kristian Bruns, Henrik Surm
 */

public class ImageJCV implements IClone, Cloneable 
{
	private IplImage image;
	
	public ImageJCV() {
		ImageGC.onCreateImage();
	}
	
	public ImageJCV(IplImage image) {
		this.image = image;
		ImageGC.onCreateImage();
	}
	
	public ImageJCV(ByteBuffer buffer) {
		this.image = (IplImage) ObjectByteConverter.bytesToObject(buffer.array());
		ImageGC.onCreateImage();
	}
	
	public ImageJCV(ImageJCV other) 
	{
		image = cvCreateImage(cvSize(other.image.width(), other.image.height()), other.image.depth(), other.image.nChannels());
		image.getByteBuffer().put(other.image.getByteBuffer());
		ImageGC.onCreateImage();
	}
	
	public ImageJCV(int width, int height)
	{
		image = cvCreateImage(cvSize(width, height), IPL_DEPTH_8U, 4);
		ImageGC.onCreateImage();
	}
	
	public ImageJCV(double[][] data) 
	{
		image = cvCreateImage(cvSize(data[0].length, data.length), IPL_DEPTH_8U, 4);
		ByteBuffer buffer = image.getByteBuffer();
		for (int i=0; i < data.length; i++) {
			buffer.putDouble(i, data[i][0]);
		}
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
			cvReleaseImage(image);
			image = null;
		}			
	}
	
	public int getNumChannels()
	{
		return image.nChannels();
	}
	
	public int getChannelDepth()
	{
		return image.depth();
	}
	
	public double[][] getMatrix() {
		throw new UnsupportedOperationException("Currenlty not implemented");
	}
	
	public IplImage getImage() {
		return image;
	}
	
	public void setImage(IplImage image) {
		this.image = image;
	}
	
	public int getWidth() {
		return this.image.width();
	}
	
	public int getHeight() {
		return this.image.height();
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
		byte[] bytes = ObjectByteConverter.objectToBytes(this.image);
		buffer.put(bytes);
	}
	
	@Override
	public String toString() {
		return "{Width: " + this.getWidth() + " Height: " + this.getHeight() + "}";
	}
	
	public void fill(int value) {
		throw new UnsupportedOperationException("Currently not implemented");
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
	private static final int checkInterval = 1000;	// Sleep for checkInterval in each iteration
	
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
