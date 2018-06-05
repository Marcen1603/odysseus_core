package de.uniol.inf.is.odysseus.receivevideostream;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;

import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;

public class ImageFrame extends CanvasFrame 
{
	private static final long serialVersionUID = -4978661992664838114L;
	
	private Object syncObj = new Object();
	private Thread grabThread;
	private boolean running;
	private FrameGrabber frameGrabber;
	
	public ImageFrame(String videoUrl) throws FrameGrabber.Exception 
	{		 
		super("Video Stream Receiver: " + videoUrl, 1.0);
		
		frameGrabber = new FFmpegFrameGrabber(videoUrl);
		frameGrabber.start();
		
		addWindowListener(new WindowListener()
		{
			@Override public void windowActivated(WindowEvent arg0) 	{}
			@Override public void windowClosing(WindowEvent arg0) 		{ onClose(); }
			@Override public void windowDeactivated(WindowEvent arg0) 	{}
			@Override public void windowDeiconified(WindowEvent arg0) 	{}
			@Override public void windowIconified(WindowEvent arg0) 	{}
			@Override public void windowOpened(WindowEvent arg0)		{}				
			@Override public void windowClosed(WindowEvent arg0)		{} 		
		});						
		setSize(400, 400);		
		setVisible(true);
		
		running = true;
		grabThread = new Thread()
		{
			@Override public void run()
			{
				ImageJCV image = null;
				while (running)
				{
					IplImage iplImage;
					try {
						iplImage = new OpenCVFrameConverter.ToIplImage().convert(frameGrabber.grab());
					} catch (FrameGrabber.Exception e) {
						ReceiveVideoStreamApp.showException(e);
						return;
					}

					if (image == null || (image.getWidth() != iplImage.width()) || (image.getHeight() != iplImage.height()) || 
							 (image.getDepth() != iplImage.depth()) || (image.getNumChannels() != iplImage.nChannels()))	
						image = ImageJCV.fromIplImage(iplImage, frameGrabber.getPixelFormat());
					else
						image.getImageData().put(iplImage.imageData().position(0).limit(iplImage.imageSize()).asByteBuffer());
					
					showImage(image);					
				}
			}
		};		
		grabThread.start();
	}
	
	protected void onClose() 
	{
		running = false;
		try {
			grabThread.join(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}


	public void showImage(ImageJCV image) 
	{		
		synchronized (syncObj) 
		{
			if (getWidth() != image.getWidth() || getHeight() != image.getHeight())
				setSize(image.getWidth(), image.getHeight());
			
			showImage(new OpenCVFrameConverter.ToIplImage().convert(image.getImage()));
			repaint();
		}
	}
}
