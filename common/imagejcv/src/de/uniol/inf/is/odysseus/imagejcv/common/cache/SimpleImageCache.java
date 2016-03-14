package de.uniol.inf.is.odysseus.imagejcv.common.cache;

import static org.bytedeco.javacpp.opencv_core.cvSize;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.bytedeco.javacpp.opencv_core.IplImage;

public class SimpleImageCache implements ImageCache
{
	private Object syncObj = new Object();
	
	Map<Integer, LinkedList<IplImage>> cache = new HashMap<>();
	
	public long allocatedMemory = 0;
	public int cacheSize = 300; 
	
	public boolean doChecks = false;
	
	private void checkLists()
	{
		int num = 0;
		for (LinkedList<IplImage> list : cache.values())
		for (IplImage image : list)
		{
			if (image.width() != 0 || image.height() != 0 || image.depth() != 0 || image.nChannels() != 0)
				num++;			
		}
		
		if (num > 0)
			System.out.println("Warning: " + num + " non-zeroed image(s) in cache!");
	}
	
	@Override
	public IplImage getImage(int width, int height, int depth, int channels)
	{
		synchronized (syncObj)
		{
			if (doChecks) checkLists();
			
			IplImage header = IplImage.createHeader(cvSize(width, height), depth, channels);		
			
			LinkedList<IplImage> list = cache.get(header.imageSize());
			
			if (list == null || list.isEmpty())
			{
				allocatedMemory += header.imageSize();
	//			System.out.println("New image created! Total mem = " + allocatedMemory);
//				System.out.print("C");
				
				return IplImage.create(cvSize(width, height), depth, channels);
			}
			else
			{
//				System.out.print("R");
	//			System.out.println("Image recycled! " + (list.size()-1) + " images left");			
				IplImage image = list.removeFirst();
				
				// DEBUG: Make sure image was added to cache properly
				if (doChecks) 
				if (image.width() != 0 || image.height() != 0 || image.depth() != 0 || image.nChannels() != 0)
					System.out.println("Warning: Non-zeroed image in cache!");
				
				// Set image parameters
				image.width(width);
				image.height(height);
				image.depth(depth);
				image.nChannels(channels);
				image.widthStep(header.widthStep());
				
				return image;
			}
		}
	}
	
	@Override
	public void putImage(IplImage image)
	{
		synchronized (syncObj)
		{
			if (doChecks) 
				checkLists();
			
			// Get list to which this image belongs 
			LinkedList<IplImage> list = cache.get(image.imageSize());
			
			// DEBUG: Make sure image is not in cache
			if (doChecks && list != null)
				for (IplImage inList : list)
				{
					if (inList == image)
						System.out.println("Warning: Double add image to cache!");
				}		
			
			// Reset image parameters so it is easy to identify references to this image in the outside world
			image.width(0);
			image.height(0);
			image.depth(0);
			image.nChannels(0);
			
			// Add image to list if there is space left, create list if there is no list
			if (list != null) {
				if (list.size() < cacheSize) {				
					list.add(image);
//					System.out.print("A");
	//				System.out.println("Image added to cache, num images = " + list.size());
				} else {
//					System.out.print("D");
	//				System.out.println("Cache full, image dismissed");
					allocatedMemory -= image.imageSize();				
				}
			} else {
				list = new LinkedList<IplImage>();
				list.add(image);
//				System.out.print("A");
				cache.put(image.imageSize(), list);
			}		
		}
	}
}
