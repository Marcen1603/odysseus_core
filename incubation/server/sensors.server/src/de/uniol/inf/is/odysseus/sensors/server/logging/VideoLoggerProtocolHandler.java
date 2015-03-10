
package de.uniol.inf.is.odysseus.sensors.server.logging;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.FrameRecorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.sensors.common.logging.LogMetaData;
import de.uniol.inf.is.odysseus.sensors.common.logging.VideoLogMetaData;

public class VideoLoggerProtocolHandler extends LoggerProtocolHandler 
{
	public static final String NAME = "VideoLogger";
	static final Runtime RUNTIME = Runtime.getRuntime();

	Logger LOG = LoggerFactory.getLogger(VideoLoggerProtocolHandler.class);

	private FrameRecorder 		recorder = null;
	private DataOutputStream 	syncFileStream = null;	
	private double				frameRate;
		
	private String			videoFileName;
	private String			syncFileName;

	public VideoLoggerProtocolHandler() 
	{
		super();
	}

	public VideoLoggerProtocolHandler(ITransportDirection direction, IAccessPattern access, IDataHandler<Tuple<?>> dataHandler, OptionMap options) 
	{
		super(direction, access, dataHandler, options);
		
		frameRate = options.getDouble("framerate", 30.0);
	}

	@Override
	public IProtocolHandler<Tuple<?>> createInstance(ITransportDirection direction, IAccessPattern access, OptionMap options, IDataHandler<Tuple<?>> dataHandler) 
	{
		return new VideoLoggerProtocolHandler(direction, access, dataHandler, options);
	}	
	
	@Override protected LogMetaData startLoggingInternal(Tuple<?> object) throws IOException 
	{
		videoFileName = getFileNameBase() + ".mp4";
		syncFileName = getFileNameBase() + ".sync";		
				
		ImageJCV image = (ImageJCV) object.getAttribute(0);
		
		try
		{
			recorder = new FFmpegFrameRecorder(videoFileName, image.getWidth(), image.getHeight());
	        recorder.setVideoCodec(13);
	        recorder.setFrameRate(frameRate);
	        recorder.setFormat("mp4");
	        recorder.setVideoQuality(0);
	        recorder.start();
		}
		catch (FFmpegFrameRecorder.Exception e)
		{
			recorder = null;
			throw new IOException(e);
		}		
	        
		// Set up sync file
	    syncFileStream = new DataOutputStream(new FileOutputStream(syncFileName));
	    
		VideoLogMetaData logMetaData = new VideoLogMetaData();
		logMetaData.videoFile = new File(videoFileName).getName();
		logMetaData.syncFile = new File(syncFileName).getName();
	    
	    return logMetaData;
	}

	@Override protected void stopLoggingInternal(LogMetaData logMetaData) 
	{
		if (recorder != null)
		{
			try
			{
                recorder.stop();
                recorder.release();
			}
			catch (FrameRecorder.Exception e) 
			{				
			} 	
			
			recorder = null;
		}
		
		if (syncFileStream != null)
		{
			try
			{
				syncFileStream.close();
			}
			catch (IOException e) 
			{
			}
	
			syncFileStream = null;
		}		
	}

	@Override protected long writeInternal(Tuple<?> object, long timeStamp) throws IOException 
	{
		ImageJCV image = (ImageJCV) object.getAttribute(0);

		try
		{		
			recorder.record(image.getImage());
			syncFileStream.writeDouble(timeStamp / 1000.0);			
		}		
		catch (Exception e)
		{
			throw new IOException(e);
		}
		
		long length = new File(videoFileName).length();
		System.out.println("write " + image.toString() + ", video file size = " + length);
		
//		image.releaseReference();
		
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
		if (!(o instanceof VideoLoggerProtocolHandler)) {
			return false;
		}
/*		VideoLogger other = (VideoLogger) o;
		if (this.nanodelay != other.getNanodelay()
				|| this.delay != other.getDelay()
				|| this.delayeach != other.getDelayeach()
				|| this.dumpEachLine != other.getDumpEachLine()
				|| this.measureEachLine != other.getMeasureEachLine()
				|| this.lastLine != other.getLastLine()
				|| this.debug != other.isDebug()
				|| this.readFirstLine != other.isReadFirstLine()) {
			return false;
		}*/
		return true;
	}
}
