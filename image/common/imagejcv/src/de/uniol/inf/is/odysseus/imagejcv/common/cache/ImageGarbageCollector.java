package de.uniol.inf.is.odysseus.imagejcv.common.cache;

//This static class monitors the number of un-finalized ImageJCV objects. Since native memory allocation will not trigger
//GC, it has to be done manually. When there are enough un-finalized objects, a thread is started, which monitors the image 
//count and calls System.gc() when a threshold is reached. If there are only a few objects left, the thread finishes on its own.
//To monitor the number of images, onCreateImage and onReleaseImage have to be called in constructor/finalize methods.
//
//NOTE: If the GC is triggered by normal java heap allocations, native code objects destroyed by java finalizers will also be collected.
//    If there is enough "other" memory activity, this class may not be needed.
@SuppressWarnings(value = { "unused" })
public class ImageGarbageCollector
{
	private static final int startThreshold = 20; //20;	// Only start thread when imageCount>startThreshold. Stop running thread when imageCount<=startThreshold. 
	private static final int gcThreshold = 50; //200;		// Run System.gc() when imageCount>gcThreshold. Threshold should be a lot larger than the normal number of concurrent images in the system. 
	private static final int checkInterval = 100;	// Sleep for checkInterval in each iteration
	
	private static Thread gcThread = null;
	
	private static int imageCount = 0;
	private static int lastImageCount = 0;
	
	private static Object syncObject = new Object();

	private static void onGC()
	{
		long start = System.nanoTime();
		System.out.print("System.gc");
		System.gc();
		System.out.println(" time = " + (System.nanoTime() - start) / 1.0e6 + " ms");		
	}
	
	public static void onCreateImage()
	{
		synchronized (syncObject)
		{
			imageCount++;
			if (imageCount > 200)
			{
				imageCount = 0;
				
//				onGC();
				
				new Thread()
				{
					@Override public void run()
					{
						onGC();
					}
				}.start();
			}
		}
	}
}