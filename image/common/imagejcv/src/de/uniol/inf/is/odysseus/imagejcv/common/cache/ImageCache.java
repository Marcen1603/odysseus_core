package de.uniol.inf.is.odysseus.imagejcv.common.cache;

import org.bytedeco.javacpp.opencv_core.IplImage;

public interface ImageCache 
{
	public IplImage getImage(int width, int height, int depth, int channels);
	public void putImage(IplImage image);
}
