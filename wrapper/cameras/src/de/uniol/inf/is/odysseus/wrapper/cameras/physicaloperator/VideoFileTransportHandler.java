package de.uniol.inf.is.odysseus.wrapper.cameras.physicaloperator;

import java.io.File;
import java.io.IOException;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractSimplePullTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.image.common.datatype.Image;

@SuppressWarnings("unused")
public class VideoFileTransportHandler extends AbstractSimplePullTransportHandler<Tuple<IMetaAttribute>> 
{
//	@SuppressWarnings("unused")
	private final Logger logger = LoggerFactory.getLogger(VideoFileTransportHandler.class);
	private final Object processLock = new Object();
	
	private FFmpegFrameGrabber 	frameGrabber;
	private String videoUrl;
	private double fps;
	
	private long startTime;
	private double currentTime; // Current timestamp in seconds
	private String syncFileName = null;
	
	public VideoFileTransportHandler() 
	{
		super();
	}
	
	/**
	 * @param protocolHandler
	 * @throws IOException 
	 */
	public VideoFileTransportHandler(final IProtocolHandler<?> protocolHandler, OptionMap options) 
	{
		super(protocolHandler, options);

		options.checkRequiredException("videourl");
		
		videoUrl = options.get("videourl");
		fps = options.getDouble("fps", 0.0);
		
		String timeStampMode = options.get("timestampmode", "start");
		
		if (timeStampMode.equals("start"))
		{
			// Frame timestamps will be extrapolated from beginning time stamp specified in the options
			// If no timestamp is given, system time will be used
			// curTime = startTime + frameNum/fps
			startTime = options.getLong("startTime", 0);
		}
		else
		if (timeStampMode.equals("filetime"))
		{
			// Frame timestamps will be extrapolated from the file last modified time stamp
			// curTime = startTime + frameNum/fps
			startTime = new File(videoUrl).lastModified();
			
			if (startTime == 0)
				throw new IllegalArgumentException("Could not read file last modified timestamp");
		}
		else
		if (timeStampMode.equals("syncfile"))
		{
			// Frame timestamps will be read from synchronization file
			syncFileName = options.get("syncfilename");
		}
		else
			throw new IllegalArgumentException("timeStampMode has an invalid value: " + timeStampMode);
	}
	

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) 
	{
		return new VideoFileTransportHandler(protocolHandler, options);
	}


	@Override public String getName() { return "VideoFile"; }

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
			
			if (fps == 0.0)
				fps = frameGrabber.getFrameRate();
			
			if (syncFileName != null)
			{
				// TODO: syncFile nutzen
			}
			
			currentTime = startTime / 1000.0;
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
	
	@Override public Tuple<IMetaAttribute> getNext() 
	{
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
		
		Tuple<IMetaAttribute> tuple = new Tuple<>(1, false);
        tuple.setAttribute(0, image);
        
        long longTimeStamp = (long) (currentTime * 1000.0);
        
        TimeInterval timeStamp = new TimeInterval(new PointInTime(longTimeStamp));
        tuple.setMetadata(timeStamp);
        
        if (syncFileName != null)
        {
        	// TODO
//        	currentTime = syncFileStream.readDouble();
        }	
        else
        {
        	currentTime += 1.0 / fps;
        }
        
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
