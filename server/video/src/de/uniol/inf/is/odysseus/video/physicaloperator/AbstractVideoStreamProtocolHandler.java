
package de.uniol.inf.is.odysseus.video.physicaloperator;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import org.bytedeco.javacpp.opencv_core.IplImage;
import static org.bytedeco.javacpp.avcodec.AV_CODEC_ID_NONE;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;

public abstract class AbstractVideoStreamProtocolHandler extends AbstractProtocolHandler<Tuple<?>> 
{
	public static final String OPTIONS_PREFIX = "codec:";
	
	private enum TimeStampMode
	{
		start, fileTime, syncFile, none
	};	
	
	private static final Logger LOG = LoggerFactory.getLogger(AbstractVideoStreamProtocolHandler.class);	
	
	private final Object processLock = new Object();

	private String streamUrl;
	private boolean useDelay;
	private double frameRate;
	private int bitRate;
	private String format;	
	private int videoCodec;
	private double videoQuality;
	private Map<String, String> videoOptions = new HashMap<>();	
	private TimeStampMode timeStampMode;
	private long startTime;			// Time stamp for first frame
	private String syncFileName = null;
	
	private FrameRecorder recorder;
	private FrameGrabber grabber;
//	private Tuple<IMetaAttribute> currentTuple;
	
	private LinkedList<Tuple<IMetaAttribute>> tupleQueue = new LinkedList<>();
	
	private ImageJCV receivedImage;
	private boolean isDone;
	private double currentTime; // Current frame timestamp in seconds
	private double currentFrameSystemTime; // System timestamp for current frame
	
	public AbstractVideoStreamProtocolHandler() 
	{
		super();
	}

	public AbstractVideoStreamProtocolHandler(ITransportDirection direction, IAccessPattern access, IDataHandler<Tuple<?>> dataHandler, 
												OptionMap options) 
	{
		super(direction, access, dataHandler, options);

		options.checkRequiredException("streamurl");

		frameRate = options.getDouble("framerate", 0.0);
		streamUrl = options.get("streamurl");
		useDelay = options.getBoolean("usedelay",  false);
		bitRate = options.getInt("bitRate", 0);
		videoCodec = options.getInt("videoCodec", AV_CODEC_ID_NONE);
		videoQuality = options.getDouble("videoQuality", -1.0);
		format = options.get("format", null); 
		
		for (String key : options.getUnreadOptions())
		{
			if (key.startsWith(OPTIONS_PREFIX))
				videoOptions.put(key.substring(OPTIONS_PREFIX.length()), options.get(key));
		}		
		
		String timeStampModeStr = options.get("timestampmode", "start");
		
			 if (timeStampModeStr.equals("start"))		timeStampMode = TimeStampMode.start;
		else if (timeStampModeStr.equals("filetime"))	timeStampMode = TimeStampMode.fileTime;
		else if (timeStampModeStr.equals("syncfile"))	timeStampMode = TimeStampMode.syncFile;
		else if (timeStampModeStr.equals("none"))		timeStampMode = TimeStampMode.none;
		else
			throw new IllegalArgumentException("timeStampMode has an invalid value: " + timeStampModeStr);		
	}
	
	@Override public void open() throws UnknownHostException, IOException 
	{
		recorder = null;
		grabber = null;
		isDone = false;
				
		if (getDirection().equals(ITransportDirection.IN))
		{
			if (getAccessPattern().equals(IAccessPattern.PUSH) || getAccessPattern().equals(IAccessPattern.ROBUST_PUSH))
				throw new UnsupportedOperationException(getName() + " cannot be run in Push mode!");			
			
			switch (timeStampMode)
			{
			case start:
				// Frame timestamps will be extrapolated from beginning time stamp specified in the options
				// If no timestamp is given, system time will be used
				// curTime = startTime + frameNum/fps
				startTime = getOptionsMap().getLong("starttime", -1);
				break;
				
			case fileTime:
				// Frame timestamps will be extrapolated from the file last modified time stamp
				// curTime = startTime + frameNum/fps
				startTime = new File(streamUrl).lastModified();
				
				if (startTime == 0)
					throw new IOException("Could not read last modified timestamp from \"" + streamUrl + "\"");
				break;
	
			case syncFile:
				// Frame timestamps will be read from synchronization file
				syncFileName = getOptionsMap().get("syncfilename");
				// TODO: Read first timestamp
				throw new UnsupportedOperationException("Timestamp mode \"syncfile\" not yet implemented for reading!");
//				break;
				
			case none:
				break;
			default:
				break;
			}
	
			if (startTime == -1)
				startTime = System.currentTimeMillis();				
			
			currentTime = startTime / 1000.0;	
		}
	}
	
	public void close() throws IOException 
	{
		synchronized (processLock) 
		{
			if (recorder != null) 
			{
				try	{
					recorder.stop();
					recorder.release();
				} catch (FrameRecorder.Exception e) {
					throw new IOException(e);
				} finally {
					recorder = null;
				}
			} 

			if (grabber != null) 
			{
				try	{
					grabber.stop();
					grabber.release();
				} catch (FrameGrabber.Exception e) {
					throw new IOException(e);
				} finally {
					grabber = null;
				}
			} 
			
			isDone = true;
		}
	}	
	
	public abstract FrameRecorder createRecorder(ImageJCV image);
	
	private void startRecorder(ImageJCV image) throws FrameRecorder.Exception
	{
		LOG.info("Start streaming to " + streamUrl + ", w = " + image.getWidth() + ", h = " + image.getHeight());
		
		recorder = createRecorder(image);
		
		if (format != null) recorder.setFormat(format);
		if (videoCodec != AV_CODEC_ID_NONE) recorder.setVideoCodec(videoCodec);
		if (videoQuality != -1.0) recorder.setVideoQuality(videoQuality);
		if (frameRate != 0.0) recorder.setFrameRate(frameRate);
		if (bitRate != 0) recorder.setVideoBitrate(bitRate);
		
		for (Entry<String, String> entry : videoOptions.entrySet())
			recorder.setVideoOption(entry.getKey(), entry.getValue());
		
		recorder.start();
							
		LOG.info("Streaming to " + streamUrl + " is running");
	}	
	
	@Override
	public void write(Tuple<?> object) throws IOException 
	{
		synchronized (processLock)
		{
			try
			{
				ImageJCV image = (ImageJCV) object.getAttribute(0);
				
				if (recorder == null)
					startRecorder(image);
				
				recorder.record(image.getImage());
				LOG.debug("Frame written: " + image);
			}
			catch (Exception e)
			{
				throw new IOException("Error while recording frame", e);
			}
		}
	}
	
	public abstract FrameGrabber createGrabber();
	
	private void startGrabber() throws FrameGrabber.Exception
	{
		LOG.info("Starting grabber for " + streamUrl);
		grabber = createGrabber();
		grabber.start();		

		if (frameRate == 0.0)
		{
			frameRate = grabber.getFrameRate();
			if (frameRate == 0.0)
			{
				if (timeStampMode == TimeStampMode.start || timeStampMode == TimeStampMode.fileTime)
					throw new IllegalArgumentException("Video source doesn't provide frame rate. Can't use \"start\" or \"fileTime\" as timeStampMode!");
				if (useDelay)
					throw new IllegalArgumentException("Video source doesn't provide frame rate. Can't use \"useDelay\", since frame length is unknown!");
			}
				
		}
		
		LOG.info("Grabber for " + streamUrl + " started!");
	}

	
	@Override
	public boolean hasNext() throws IOException 
	{
		synchronized (processLock)
		{
			if (isDone) return true;
			
//			if (this instanceof FFmpegVideoStreamProtocolHandler)
//				System.out.println("hasNext enter");
			
			try
			{
				// Start grabber if not started yet
				if (grabber == null)
					startGrabber();				
				
				// Apply delay if necessary. Grabber has to be started before since frameRate is needed here
				if (useDelay)
					try 
					{
						double frameTime = 1.0 / frameRate;
						while (System.currentTimeMillis() / 1000.0 - currentFrameSystemTime < frameTime)
							Thread.sleep(1);
					} 
					catch (InterruptedException e) 
					{
						e.printStackTrace();
						Thread.currentThread().interrupt();
						return false;
					}						
								
				// Grab image
				IplImage iplImage = grabber.grab();
				if (iplImage == null || iplImage.isNull())
				{
					isDone = true;
					return false;
				}
				
				LOG.debug("Frame received at " + System.currentTimeMillis());
				
				// Create result tuple and image, if not created yet
				Tuple<IMetaAttribute> currentTuple = new Tuple<>(getSchema().size(), false);
				int[] attrs = getSchema().getSDFDatatypeAttributePositions(SDFImageJCVDatatype.IMAGEJCV);
				if (attrs.length > 0)
				{
					if (receivedImage == null || (receivedImage.getWidth() != iplImage.width()) || (receivedImage.getHeight() != iplImage.height()) || 
							 (receivedImage.getDepth() != iplImage.depth()) || (receivedImage.getNumChannels() != iplImage.nChannels()))	
						receivedImage = new ImageJCV(iplImage.width(), iplImage.height(), iplImage.depth(), iplImage.nChannels());
			
					receivedImage.getImageData().rewind();
					receivedImage.getImageData().put(iplImage.getByteBuffer());									
					currentTuple.setAttribute(attrs[0], receivedImage);
				}
					
				// Set timestamps
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
			        	currentTime += 1.0 / frameRate;
			        }
				        
			        long endTimeStamp = (long) (currentTime * 1000.0);
			        
					attrs = getSchema().getSDFDatatypeAttributePositions(SDFDatatype.START_TIMESTAMP);
					if (attrs.length > 0) 
						currentTuple.setAttribute(attrs[0], startTimeStamp);					
			        
					attrs = getSchema().getSDFDatatypeAttributePositions(SDFDatatype.END_TIMESTAMP);
					if (attrs.length > 0) 
						currentTuple.setAttribute(attrs[0], endTimeStamp);
				}
				
				currentFrameSystemTime = System.currentTimeMillis() / 1000.0;

				tupleQueue.addLast(currentTuple);
				
	//			if (this instanceof FFmpegVideoStreamProtocolHandler)
	//				System.out.println("hasNext leave true");
				return true;
			}
			catch (Exception e) 
			{
				isDone = true;
				throw new IOException(e);
			}
		}
	}		

	@Override
	public Tuple<?> getNext() throws IOException 
	{
		synchronized (processLock)
		{
	        return isDone ? null : tupleQueue.pop();
		}
	}

	@Override
	public ITransportExchangePattern getExchangePattern() {
		if (this.getDirection().equals(ITransportDirection.IN)) {
			return ITransportExchangePattern.InOnly;
		} else {
			return ITransportExchangePattern.OutOnly;
		}
	}

	@Override
	public void onConnect(ITransportHandler caller) 
	{
	}

	@Override
	public void onDisonnect(ITransportHandler caller) 
	{
	}

	@Override public boolean isDone() 
	{
		return isDone;
	}
	
	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> o) 
	{
		if (!(o instanceof AbstractVideoStreamProtocolHandler)) 
			return false;
		
		AbstractVideoStreamProtocolHandler other = (AbstractVideoStreamProtocolHandler) o;
		if (!streamUrl.equals(other.streamUrl) || frameRate != other.frameRate || 
		    (bitRate != other.bitRate) || !format.equals(other.format) || !videoOptions.equals(other.videoOptions))
			return false;
		
		return true;
	}

	public String getStreamUrl() 
	{
		return streamUrl;
	}

	public void setStreamUrl(String streamUrl) 
	{
		throw new UnsupportedOperationException("Not implemented yet!");
//		this.streamUrl = streamUrl;
	}

	public double getFrameRate() 
	{
		return frameRate;
	}

	public void setFrameRate(double frameRate) 
	{
		throw new UnsupportedOperationException("Not implemented yet!");
//		this.frameRate = frameRate;
	}

	public FrameRecorder getRecorder() 
	{
		return recorder;
	}
}
