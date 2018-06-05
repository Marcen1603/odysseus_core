package de.uniol.inf.is.odysseus.imagejcv.common.cache;

import static org.bytedeco.javacpp.opencv_core.cvSize;

import org.bytedeco.javacpp.opencv_core.IplImage;

public class NoImageCache implements ImageCache
{
	@Override
	public synchronized IplImage getImage(int width, int height, int depth, int channels)
	{
		return IplImage.create(cvSize(width, height), depth, channels);
	}
	
	@Override
	public synchronized void putImage(IplImage image)
	{
		return;
	}
}
