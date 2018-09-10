package de.uniol.inf.is.odysseus.imagejcv.common.cache;

public class ImageCacheProvider 
{
	private static ImageCache instance = null;

	public static ImageCache getInstance()
	{
		if (instance == null)
		{
//			instance = new NoImageCache();
			instance = new SimpleImageCache();
		}
	
		return instance;
	}		
}
