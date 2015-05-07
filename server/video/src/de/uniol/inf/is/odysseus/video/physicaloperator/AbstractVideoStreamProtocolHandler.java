
package de.uniol.inf.is.odysseus.video.physicaloperator;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

import org.bytedeco.javacpp.opencv_core.IplImage;
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
	private enum TimeStampMode
	{
		start, fileTime, syncFile, none
	};	
	
	private static final Logger LOG = LoggerFactory.getLogger(AbstractVideoStreamProtocolHandler.class);	
	
	private final Object processLock = new Object();

	private String streamUrl;
	private boolean useDelay;
	private double frameRate;
	private TimeStampMode timeStampMode;
	
	private FrameRecorder recorder;
	private FrameGrabber grabber;
	private Tuple<IMetaAttribute> currentTuple; 
	private ImageJCV image;
	
	private long startTime;
	private double currentTime; // Current timestamp in seconds
	private String syncFileName = null;
	
	
	public AbstractVideoStreamProtocolHandler() 
	{
		super();
	}

	public AbstractVideoStreamProtocolHandler(ITransportDirection direction, IAccessPattern access, IDataHandler<Tuple<?>> dataHandler, OptionMap options) 
	{
		super(direction, access, dataHandler, options);

		options.checkRequired("streamurl");
		
		frameRate = options.getDouble("framerate", 0.0);
		streamUrl = options.get("streamurl");
		useDelay = options.getBoolean("usedelay",  false);
		
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
				startTime = getOptionsMap().getLong("starttime", 0);
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
		}
	}	
	
	public abstract FrameRecorder startRecorder(ImageJCV image) throws FrameRecorder.Exception;
	public abstract void recordFrame(ImageJCV image) throws FrameRecorder.Exception;
	
	@Override
	public void write(Tuple<?> object) throws IOException 
	{
		synchronized (processLock)
		{
			try
			{
				ImageJCV image = (ImageJCV) object.getAttribute(0);
				
				if (recorder == null)
				{
					int w = image.getWidth();
					int h = image.getHeight();
					LOG.debug("Start streaming to " + streamUrl + ", w = " + w + ", h = " + h);
					
					recorder = startRecorder(image);
					
					LOG.debug("Streaming to " + getStreamUrl() + " is running");
				}
	
				recordFrame(image);
			}
			catch (Exception e)
			{
				throw new IOException("Error while recording frame", e);
			}
		}
	}
	
	public abstract FrameGrabber startGrabber() throws FrameGrabber.Exception;
	
	@Override
	public boolean hasNext() throws IOException 
	{
		synchronized (processLock)
		{
			try
			{
				// Apply delay if necessary
				if (useDelay)
					try 
					{
						Thread.sleep((long)(1000.0 / frameRate));
					} 
					catch (InterruptedException e) 
					{
						e.printStackTrace();
						Thread.currentThread().interrupt();
					}						
				
				// Start grabber if not started yet
				if (grabber == null)
				{
					grabber = startGrabber();
					if (frameRate == 0.0)
						frameRate = grabber.getFrameRate();					
				}
				
				// Grab image
				IplImage iplImage = grabber.grab();
				if (iplImage == null || iplImage.isNull()) return false;
				
				// Create result tuple and image, if not created yet
				currentTuple = new Tuple<>(getSchema().size(), false);
				int[] attrs = getSchema().getSDFDatatypeAttributePositions(SDFImageJCVDatatype.IMAGEJCV);
				if (attrs.length > 0)
				{
					if (image == null || (image.getWidth() != iplImage.width()) || (image.getHeight() != iplImage.height()) || 
							 (image.getDepth() != iplImage.depth()) || (image.getNumChannels() != iplImage.nChannels()))	
						image = new ImageJCV(iplImage.width(), iplImage.height(), iplImage.depth(), iplImage.nChannels());
			
					image.getImageData().rewind();
					image.getImageData().put(iplImage.getByteBuffer());									
					currentTuple.setAttribute(attrs[0], image);
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
					if (attrs.length > 0) currentTuple.setAttribute(attrs[0], startTimeStamp);					
			        
					attrs = getSchema().getSDFDatatypeAttributePositions(SDFDatatype.END_TIMESTAMP);
					if (attrs.length > 0) currentTuple.setAttribute(attrs[0], endTimeStamp);
				}

				return true;
			}
			catch (Exception e) 
			{
				throw new IOException(e);
			}
		}
	}		

	@Override
	public Tuple<?> getNext() throws IOException 
	{
		Tuple<IMetaAttribute> tuple = currentTuple;
		currentTuple = null;		
        return tuple;
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
		return (grabber != null) ? (grabber.getFrameNumber() < grabber.getLengthInFrames()) : true;
	}
	
	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> o) 
	{
		if (!(o instanceof AbstractVideoStreamProtocolHandler)) 
			return false;
		
		AbstractVideoStreamProtocolHandler other = (AbstractVideoStreamProtocolHandler) o;
		if (!streamUrl.equals(other.streamUrl) || frameRate != other.frameRate)
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
