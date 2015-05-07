
package de.uniol.inf.is.odysseus.video.physicaloperator;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.FrameRecorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;

public class VideoStreamProtocolHandler extends AbstractProtocolHandler<Tuple<?>> 
{
	public static final String NAME = "VideoStream";
	public static final String PREFIX = "ffmpeg:";
	
	static final Runtime RUNTIME = Runtime.getRuntime();

	Logger LOG = LoggerFactory.getLogger(VideoStreamProtocolHandler.class);
	private final Object processLock = new Object();

	private FFmpegFrameRecorder	recorder;
	private String streamUrl;
	private double frameRate;
	private int bitRate;
	private String format;
	
	private Map<String, String> videoOptions = new HashMap<>();

	public VideoStreamProtocolHandler() 
	{
		super();
	}

	public VideoStreamProtocolHandler(ITransportDirection direction, IAccessPattern access, IDataHandler<Tuple<?>> dataHandler, OptionMap options) 
	{
		super(direction, access, dataHandler, options);
		
		options.checkRequired("streamurl");
		
		frameRate = options.getDouble("framerate", 0.0);
		streamUrl = options.get("streamurl");
		bitRate = options.getInt("bitRate", 0);
		format = options.get("format", "h264");
		
		for (String key : options.getUnreadOptions())
		{
			if (key.startsWith(PREFIX))
				videoOptions.put(key.substring(PREFIX.length()), options.get(key));
		}
	}
	
	@Override
	public IProtocolHandler<Tuple<?>> createInstance(ITransportDirection direction, IAccessPattern access, OptionMap options, IDataHandler<Tuple<?>> dataHandler)
	{
		return new VideoStreamProtocolHandler(direction, access, dataHandler, options);
	}	
	
	@Override public void open() throws UnknownHostException, IOException 
	{
		System.out.println("VideoStreamTransportHandler.open enter");
		recorder = null;
		System.out.println("VideoStreamTransportHandler.open leave");
	}
	
	@Override
	public void close() throws IOException 
	{
		System.out.println("VideoStreamTransportHandler.close enter");
		
		if (recorder != null) {
			try {
				synchronized (processLock) {
					recorder.stop();
					recorder.release();
				}
			} catch (FrameRecorder.Exception e) {
				throw new IOException(e);
			} finally {
				recorder = null;
			}
		}
		
		System.out.println("VideoStreamTransportHandler.close leave");
	}	
	
	private void setUpStream(ImageJCV image) throws FrameRecorder.Exception
	{
		int w = image.getWidth();
		int h = image.getHeight();
		System.out.println("Start streaming server @ " + streamUrl + ", w = " + w + ", h = " + h);

//		recorder.setPixelFormat(avutil.PIX_FMT_YUV420P16);
//		recorder.setVideoCodec(13);		
//		recorder.setVideoOption("preset", "ultrafast");
//		recorder.setVideoOption("tune", "zerolatency");
		
		recorder = new FFmpegFrameRecorder(streamUrl, w, h);
		recorder.setFormat(format);
		
		if (frameRate != 0.0) recorder.setFrameRate(frameRate);
		if (bitRate != 0) recorder.setVideoBitrate(bitRate);
		
		for (Entry<String, String> entry : videoOptions.entrySet())
			recorder.setVideoOption(entry.getKey(), entry.getValue());
		
		recorder.start();
			
        System.out.println("Streaming server is running");
	}
	
	@Override
	public void write(Tuple<?> object) throws IOException 
	{
		ImageJCV image = (ImageJCV) object.getAttribute(0);
		
		synchronized (processLock)
		{
			if (recorder == null)
			{
				try
				{
					setUpStream(image);
				}
				catch (FrameRecorder.Exception e)
				{
					throw new IOException(e);
				}
			}
			
			try
			{
				recorder.record(image.getImage());
			}
			catch (Exception e)
			{
				System.out.println("Error while recording frame: " + e.getMessage());
			}
		}
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
		if (!(o instanceof VideoStreamProtocolHandler)) {
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
