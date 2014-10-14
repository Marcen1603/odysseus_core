package de.uniol.inf.is.odysseus.video.physicaloperator;

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
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;

@SuppressWarnings("unused")
public class VideoFileTransportHandler extends AbstractSimplePullTransportHandler<Tuple<IMetaAttribute>> 
{
	private enum TimeStampMode
	{
		start, fileTime, syncFile, none
	};
	
//	@SuppressWarnings("unused")
	private final Logger logger = LoggerFactory.getLogger(VideoFileTransportHandler.class);
	private final Object processLock = new Object();
	
	private String videoUrl;
	private TimeStampMode timeStampMode;
	private double fps;
	private boolean useDelay;

	private FFmpegFrameGrabber 	frameGrabber;
	private long startTime;
	private double currentTime; // Current timestamp in seconds
	private String syncFileName = null;
	
	private ImageJCV currentImage = null;
	
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
		useDelay = options.getBoolean("usedelay",  false);
		
		String timeStampModeStr = options.get("timestampmode", "start");
		
			 if (timeStampModeStr.equals("start"))		timeStampMode = TimeStampMode.start;
		else if (timeStampModeStr.equals("filetime"))	timeStampMode = TimeStampMode.fileTime;
		else if (timeStampModeStr.equals("syncfile"))	timeStampMode = TimeStampMode.syncFile;
		else if (timeStampModeStr.equals("none"))		timeStampMode = TimeStampMode.none;
		else
			throw new IllegalArgumentException("timeStampMode has an invalid value: " + timeStampModeStr);
	}
	

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) 
	{
		return new VideoFileTransportHandler(protocolHandler, options);
	}

	@Override public String getName() { return "VideoFile"; }

	@Override public void processInOpen() throws IOException 
	{
		switch (timeStampMode)
		{
		case start:
			// Frame timestamps will be extrapolated from beginning time stamp specified in the options
			// If no timestamp is given, system time will be used
			// curTime = startTime + frameNum/fps
			startTime = getOptionsMap().getLong("starttime", 0);
			break;
			
		case fileTime:
			// Frame timestamps will be extrapolated from the file last modified time stamp
			// curTime = startTime + frameNum/fps
			startTime = new File(videoUrl).lastModified();
			
			if (startTime == 0)
				throw new IOException("Could not read last modified timestamp from \"" + videoUrl + "\"");
			break;

		case syncFile:
			// Frame timestamps will be read from synchronization file
			syncFileName = getOptionsMap().get("syncfilename");
			// TODO: Read first timestamp
			break;
			
		case none:
			break;
		default:
			break;
		}

		if (startTime == 0)
			startTime = System.currentTimeMillis();
		
		currentTime = startTime / 1000.0;
		
		synchronized (processLock)
		{
			frameGrabber = new FFmpegFrameGrabber(videoUrl);
			try 
			{
				frameGrabber.start();
				double gamma = frameGrabber.getGamma();
				System.out.println("Gamma = " + gamma);
			} 
			catch (FrameGrabber.Exception e) 
			{
				frameGrabber = null;
				throw new IOException(e.getMessage());
			}
			
			if (fps == 0.0)
				fps = frameGrabber.getFrameRate();
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
		synchronized (processLock)
		{
			if (currentImage == null) return null;
			
			Tuple<IMetaAttribute> tuple;        
	
			if (timeStampMode != TimeStampMode.none)
			{		
		        long startTimeStamp = (long) (currentTime * 1000.0);        		
					        	        
		        if (syncFileName != null)
		        {
		        	// TODO
		//        	currentTime = syncFileStream.readDouble();
		        }	
		        else
		        {
		        	currentTime += 1.0 / fps;
		        }
		        
		        long endTimeStamp = (long) (currentTime * 1000.0);
		        
		        tuple = new Tuple<>(3, false);
		        tuple.setAttribute(0, currentImage);
		        tuple.setAttribute(1, startTimeStamp);
		        tuple.setAttribute(2, endTimeStamp);	        
			}
			else
			{
				tuple = new Tuple<>(1, false);
				tuple.setAttribute(0, currentImage);
			}
			
			currentImage = null;
	
	        return tuple;
		}
	}
    
	@Override public boolean hasNext() 
	{
		if (useDelay)
			try 
			{
				Thread.sleep((long)(1000.0 / fps));
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
			
		synchronized (processLock)
		{		
			try 
			{
				if (frameGrabber == null) return false;
				IplImage iplImage = frameGrabber.grab().clone();
				if (iplImage == null || iplImage.isNull()) return false;
				
				currentImage = new ImageJCV(iplImage);
				return true;
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				return false;
			}		
		}		
	}
		
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
