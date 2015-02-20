package de.uniol.inf.is.odysseus.video.physicaloperator;



import java.io.File;
import java.io.IOException;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;



@SuppressWarnings("unused")
public class VideoFileTransportHandler extends FrameGrabberTransportHandler 
{
	private enum TimeStampMode
	{
		start, fileTime, syncFile, none
	};
	
//	@SuppressWarnings("unused")
	private final Logger logger = LoggerFactory.getLogger(VideoFileTransportHandler.class);	
	
	private String videoUrl;
	private TimeStampMode timeStampMode;
	private boolean useDelay;
	
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
		
		fps = options.getDouble("fps", 0.0);
		videoUrl = options.get("videourl");		
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
		super.processInOpen();
		
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
	}
	
	@Override
	protected FrameGrabber getFrameGrabber() 
	{
		return new FFmpegFrameGrabber(videoUrl);
	}	
	   
	@Override
	protected GrabResult getFrame() throws FrameGrabber.Exception 
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
		
		GrabResult result = new GrabResult();
		result.image = frameGrabber.grab();
		
		if (timeStampMode != TimeStampMode.none)
		{		
			result.startTimeStamp = (long) (currentTime * 1000.0);        		
					        	        
	        if (syncFileName != null)
	        {
		        	// TODO
	//        	currentTime = syncFileStream.readDouble();
	        }	
	        else
	        {
	        	currentTime += 1.0 / fps;
	        }
		        
	        result.endTimeStamp = (long) (currentTime * 1000.0);
		}
		else
		{
			result.startTimeStamp = null;
			result.endTimeStamp = null;
		}
		
		return result;
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
