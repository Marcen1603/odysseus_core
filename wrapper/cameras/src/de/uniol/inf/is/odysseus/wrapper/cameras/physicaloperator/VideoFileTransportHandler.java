package de.uniol.inf.is.odysseus.wrapper.cameras.physicaloperator;

import java.io.IOException;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractSimplePullTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.image.common.datatype.Image;

@SuppressWarnings("unused")
public class VideoFileTransportHandler extends AbstractSimplePullTransportHandler<Tuple<?>> 
{
//	@SuppressWarnings("unused")
	private final Logger logger = LoggerFactory.getLogger(VideoFileTransportHandler.class);
	private final Object processLock = new Object();
	
	private FFmpegFrameGrabber 	frameGrabber;
	private String videoUrl;
	private double fps;
	
	private long startTime;
	private String syncFileName;
	
	public VideoFileTransportHandler() 
	{
		super();
	}
	
	/**
	 * @param protocolHandler
	 * @throws IOException 
	 */
	public VideoFileTransportHandler(final IProtocolHandler<?> protocolHandler, OptionMap options) throws IOException 
	{
		super(protocolHandler, options);

		videoUrl = options.get("videourl");
		if (videoUrl == null) throw new IOException("Parameter not specified in Options: videoUrl");
		
		fps = options.getDouble("fps", 0.0);
		if (fps == 0.0)
		{
			// Open video file and extract frame rate meta data
		}
		
		String timeStampMode = options.get("timestampmode", "start");
		
		if (timeStampMode.equals("start"))
		{
			// Frame timestamps will be extrapolated from beginning time stamp specified in the options
			// If no timestamp is given, system time will be used
			// curTime = startTime + frameNum/fps
			startTime = options.getLong("startTime", 0);
		}
		else
		if (timeStampMode.equals("metadata"))
		{
			// Frame timestamps will be extrapolated from beginning time stamp specified in video file meta data
			// curTime = startTime + frameNum/fps
			startTime = 0; // TODO
		}
		else
		if (timeStampMode.equals("syncfile"))
		{
			// Frame timestamps will be read from synchronization file
			syncFileName = options.get("syncfilename");
		}
	}
	

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) 
	{
		try
		{
			return new VideoFileTransportHandler(protocolHandler, options);
		}
		catch (IOException e)
		{
			return null;
		}
	}


	@Override public String getName() { return "IntegratedCamera"; }

	@Override public void processInOpen() throws IOException 
	{
		synchronized (processLock)
		{
			frameGrabber = new FFmpegFrameGrabber(videoUrl);
			try 
			{
				frameGrabber.start();
			} 
			catch (FrameGrabber.Exception e) 
			{
				frameGrabber = null;
				throw new IOException(e.getMessage());
			}
		}
	}
	
	@Override public void processInClose() throws IOException 
	{
		synchronized (processLock)
		{
			if (frameGrabber != null)
			{
				try 
				{
					frameGrabber.stop();
					frameGrabber.release();
				} 
				catch (Exception e)
				{
					throw new IOException(e.getMessage());
				}
				finally 
				{				
					frameGrabber = null;
				}
			}
		}
	}
	
	@Override public Tuple<?> getNext() 
	{
		try 
		{
			int delay = 33;
			if (delay  > 0) 
				Thread.sleep(delay);
		}
		catch (InterruptedException e) 
		{
			// interrupting the delay might be correct
			// e.printStackTrace();
		}		
		
		IplImage iplImage = null;
		try 
		{
			synchronized (processLock)
			{
				if (frameGrabber == null) return null;
				iplImage = frameGrabber.grab();
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}		
		if (iplImage == null || iplImage.isNull()) return null;
		
		Image image = new Image(iplImage.getBufferedImage());
		
		@SuppressWarnings("rawtypes")
		Tuple<?> tuple = new Tuple(1, false);
        tuple.setAttribute(0, image);
        return tuple;		
	}
    
	@Override public boolean hasNext() { return frameGrabber != null; }
		
    @Override
    public boolean isSemanticallyEqualImpl(ITransportHandler o) {
    	if(!(o instanceof VideoFileTransportHandler)) {
    		return false;
    	}
    	VideoFileTransportHandler other = (VideoFileTransportHandler)o;
    	if(this.videoUrl.equals(other.videoUrl))
    		return false;
    	
    	return true;
    }
}
