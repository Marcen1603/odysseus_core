
package de.uniol.inf.is.odysseus.wrapper.cameras.physicaloperator;

import java.io.IOException;
import java.net.UnknownHostException;

import org.bytedeco.javacpp.opencv_core.IplImage;
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
import de.uniol.inf.is.odysseus.image.common.datatype.Image;

public class VideoStreamProtocolHandler extends AbstractProtocolHandler<Tuple<?>> 
{
	public static final String NAME = "VideoStream";
	static final Runtime RUNTIME = Runtime.getRuntime();

	Logger LOG = LoggerFactory.getLogger(VideoStreamProtocolHandler.class);

	private FFmpegFrameRecorder	recorder;
	private double				frameRate;
		
	private String				streamUrl;

	public VideoStreamProtocolHandler() 
	{
		super();
	}

	public VideoStreamProtocolHandler(ITransportDirection direction, IAccessPattern access, IDataHandler<Tuple<?>> dataHandler, OptionMap options) throws IOException 
	{
		super(direction, access, dataHandler, options);
		
		frameRate = options.getDouble("framerate", 30.0);
		
		String host = options.get("host");
		String port = options.get("port", "8076");
		
		if (host == null) throw new IOException("Parameter not specified in Options: host");
		
		streamUrl = "udp://" + host + ":" + port;		
	}
	
	@Override public void open() throws UnknownHostException, IOException 
	{
		super.open();
		
		recorder = null;
	}
	
	@Override
	public void close() throws IOException 
	{
		try 
		{
			recorder.stop();
			recorder.release();
		} 
		catch (FrameRecorder.Exception e) 
		{
			throw new IOException(e);
		}
		finally
		{
			recorder = null;
			super.close();			
		}
	}	
	
	void setUpStream(Image image) throws FrameRecorder.Exception
	{
		int w = image.getWidth();
		int h = image.getHeight();
		System.out.println("Start streaming server @ " + streamUrl + ", w = " + w + ", h = " + h);		
		recorder = new FFmpegFrameRecorder(streamUrl, w, h);
//		recorder.setVideoBitrate(716800);
//		recorder.setPixelFormat(avutil.PIX_FMT_YUV420P16);
//		recorder.setVideoCodec(13);
//		
		recorder.setFrameRate(frameRate);
		recorder.setFormat("h264");
        recorder.start();		
			
        System.out.println("Streaming server is running");
	}
	
	@Override
	public void write(Tuple<?> object) throws IOException 
	{
		Image image = (Image) object.getAttribute(0);
		
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
			recorder.record(IplImage.createFrom(image.getImage()));
		}
		catch (Exception e)
		{
			System.out.println("Error while recording frame: " + e.getMessage());
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
	public IProtocolHandler<Tuple<?>> createInstance(ITransportDirection direction,
													 IAccessPattern access, OptionMap options,
													 IDataHandler<Tuple<?>> dataHandler) 
	{
		try 
		{
			return new VideoStreamProtocolHandler(direction, access, dataHandler, options);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return null;
		}		
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
