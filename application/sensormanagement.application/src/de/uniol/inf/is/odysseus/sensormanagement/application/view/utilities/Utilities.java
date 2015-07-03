package de.uniol.inf.is.odysseus.sensormanagement.application.view.utilities;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.cvResize;

import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.bytedeco.javacpp.opencv_core.IplImage;

public class Utilities 
{
	// Static class, do not instantiate
	private Utilities() {}
	
	public static Date dateFromTimeInMillis(long timeInMillis)
	{
		final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		
		calendar.setTimeInMillis(timeInMillis);
		return calendar.getTime();		
	}

	public static Date dateFromDoubleTime(double timeStamp)
	{
		final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		
		calendar.setTimeInMillis((long)(timeStamp * 1000.0));
		return calendar.getTime();		
	}	
	
	public static String stringFromDoubleTime(double timeStamp)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		return sdf.format(dateFromDoubleTime(timeStamp)) + " UTC";		
	}	

	public static double doubleTimeFromDate(Date date)
	{
		final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		
		calendar.setTime(date);
		return calendar.getTimeInMillis() / 1000.0;		
	}		
	
	public static void copyFile(File source, File dest) throws IOException 
	{
	    InputStream is = null;
	    OutputStream os = null;
	    try 
	    {
	        is = new FileInputStream(source);
	        os = new FileOutputStream(dest);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) 
	        {
	            os.write(buffer, 0, length);
	        }
	    } 
	    finally 
	    {
	        is.close();
	        os.close();
	    }
	}	
	
	static public void drawImageRatio(Graphics graphics, int targetWidth, int targetHeight, IplImage image, ImageObserver observer)
	{
		int w = image.width();
		int h = image.height();
		double ratio = (double)w / h;

		w = targetWidth;
		h = (int)(w / ratio);

		if (h > targetHeight)
		{
			h = targetHeight;
			w = (int)(ratio * h);
		}
		
		int x = (targetWidth  - w) / 2;
		int y = (targetHeight - h) / 2;
		
    	if (image.width() != w || image.height() != h)
    	{
    		IplImage newImage = cvCreateImage(cvSize(w, h), image.depth(), image.nChannels());
    		cvResize(image, newImage);
    		graphics.drawImage(newImage.getBufferedImage(), x, y, observer);
    		cvReleaseImage(newImage);
    	}
    	else		
    		graphics.drawImage(image.getBufferedImage(), x, y, observer);
	}

	public static void printTimeInMillis(int y, int m, int d, int h, int min, int s)
	{
		Calendar c2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		c2.set(y, m, d, h, min, s);
		System.out.println(Long.toString(c2.getTimeInMillis()));		
	}		
}
