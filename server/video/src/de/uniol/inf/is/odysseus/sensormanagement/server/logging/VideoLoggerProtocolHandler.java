
package de.uniol.inf.is.odysseus.sensormanagement.server.logging;

import java.io.File;
import java.io.IOException;

import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.sensormanagement.common.logging.LogMetaData;
import de.uniol.inf.is.odysseus.sensormanagement.common.logging.VideoLogMetaData;
import de.uniol.inf.is.odysseus.video.AbstractVideoImplementation;

public class VideoLoggerProtocolHandler extends LoggerProtocolHandler 
{
	public static final String NAME = "VideoLogger";
	public static final Logger LOG = LoggerFactory.getLogger(VideoLoggerProtocolHandler.class);

	private AbstractVideoImplementation videoImpl;
	private String videoExtension;	
	
	private String videoFileName;
	private String syncFileName;

	public VideoLoggerProtocolHandler() 
	{
		super();
	}

	public VideoLoggerProtocolHandler(ITransportDirection direction, IAccessPattern access, IStreamObjectDataHandler<Tuple<?>> dataHandler, OptionMap optionMap) 
	{
		super(direction, access, dataHandler, optionMap);
		
		videoImpl = new AbstractVideoImplementation()
		{
			@Override public FrameRecorder createRecorder(ImageJCV image) 
			{
				return new FFmpegFrameRecorder(videoFileName, image.getWidth(), image.getHeight());
			}

			@Override public FrameGrabber createGrabber() { return null; }
			@Override public void onRecorderStarted() {}
			@Override public void onGrabberStarted() {}
			
		};
		
		videoExtension = optionMap.get("videoExtension", "mp4");		
		videoImpl.getOptions(optionMap);		
	}

	@Override
	public IProtocolHandler<Tuple<?>> createInstance(ITransportDirection direction, IAccessPattern access, OptionMap options, IStreamObjectDataHandler<Tuple<?>> dataHandler) 
	{
		return new VideoLoggerProtocolHandler(direction, access, dataHandler, options);
	}	
	
	@Override protected LogMetaData startLoggingInternal(Tuple<?> object) throws IOException 
	{
		videoFileName = getFileNameBase() + ( (videoExtension.length() > 0) ? ("." + videoExtension) : "");
		syncFileName = getFileNameBase() + ".sync";		
				
		videoImpl.setSyncFileName(syncFileName);
		
/*		ImageJCV image = (ImageJCV) object.getAttribute(0);
		
		try
		{
			options.createAndStartRecorder(new FFmpegFrameRecorder(videoFileName, image.getWidth(), image.getHeight()));
//	        recorder.setVideoCodec(13);
//	        recorder.setFrameRate(frameRate);
//	        recorder.setFormat("mp4");
//	        recorder.setVideoQuality(0);
//	        recorder.start();
		}
		catch (FFmpegFrameRecorder.Exception e)
		{
			throw new IOException(e);
		}		*/
	        	    
		VideoLogMetaData logMetaData = new VideoLogMetaData();
		logMetaData.videoFile = new File(videoFileName).getName();
		logMetaData.syncFile = new File(syncFileName).getName();
		logMetaData.doRotate180 = false;
	    
	    return logMetaData;
	}

	@Override protected void stopLoggingInternal(LogMetaData logMetaData) 
	{
		videoImpl.stop();
	}

	@Override protected long writeInternal(Tuple<?> object, long timeStamp) throws IOException 
	{
		ImageJCV image = (ImageJCV) object.getAttribute(0);
		videoImpl.record(image, timeStamp / 1000.0);
		
		long length = new File(videoFileName).length();
		System.out.println("write " + image.toString() + ", video file size = " + length + " byte 0 = " + image.getImageData().get(0));
		
		return length;
	}	
	
	@Override
	public boolean hasNext() throws IOException 
	{
		return false;
	}

	@Override
	public Tuple<?> getNext() throws IOException 
	{
		return null;
	}

	@Override
	public String getName() {
		return NAME;
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
	public void onConnect(ITransportHandler caller) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDisonnect(ITransportHandler caller) {
		// TODO Auto-generated method stub

	}

	@Override public boolean isDone() { return true; }
	
	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> o) 
	{
		if (!(o instanceof VideoLoggerProtocolHandler)) return false;
		
		VideoLoggerProtocolHandler other = (VideoLoggerProtocolHandler) o;
		if (!videoExtension.equals(other.videoExtension)) return false;
		if (!videoImpl.equals(other.videoImpl)) return false;

		return true;
	}

	// Static method to register this handler as service, since it depends on an optional bundle
	public static ServiceRegistration<?> registerService(BundleContext bundleContext) 
	{
		try {
			return bundleContext.registerService(IProtocolHandler.class.getName(), new VideoLoggerProtocolHandler(), null);
		} catch (NoClassDefFoundError e) {
			LOG.warn("VideoLoggerProtocolHandler requires sensormanagement.server feature");
			return null;
		}
	}
}
