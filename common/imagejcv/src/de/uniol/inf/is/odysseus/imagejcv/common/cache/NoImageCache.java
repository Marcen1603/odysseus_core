package de.uniol.inf.is.odysseus.imagejcv.common.cache;

import static org.bytedeco.javacpp.opencv_core.cvSize;

import org.bytedeco.javacpp.opencv_core.IplImage;

public class NoImageCache implements ImageCache
{
	public synchronized IplImage getImage(int width, int height, int depth, int channels)
	{
		return IplImage.create(cvSize(width, height), depth, channels);
	}
	
	public synchronized void putImage(IplImage image)
	{
		return;
	}
}
