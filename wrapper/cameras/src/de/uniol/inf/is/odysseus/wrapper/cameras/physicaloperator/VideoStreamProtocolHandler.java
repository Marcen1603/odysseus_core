
package de.uniol.inf.is.odysseus.wrapper.cameras.physicaloperator;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.FrameRecorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.connection.AcceptorSelectorHandler;
import de.uniol.inf.is.odysseus.core.connection.IAccessConnectionListener;
import de.uniol.inf.is.odysseus.core.connection.NioTcpConnection;
import de.uniol.inf.is.odysseus.core.connection.SelectorThread;
import de.uniol.inf.is.odysseus.core.connection.TCPAcceptor;
import de.uniol.inf.is.odysseus.core.connection.TCPAcceptorListener;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.image.common.datatype.Image;

class VideoStream
{
	private double frameRate;
	private String streamUrl;
	private FFmpegFrameRecorder recorder;

	VideoStream(String streamUrl, double frameRate)
	{
		this.streamUrl = streamUrl;
		this.frameRate = frameRate;
		
		// streamUrl = "udp://" + host + ":" + port;
	}
	
	public void open()
	{
		recorder = null;
	}
	
	public void close()
	{
		if (recorder != null)
		{
			try 
			{
				recorder.stop();
			} 
			catch (FrameRecorder.Exception e) 
			{
				e.printStackTrace();
			}
		
			try
			{
				recorder.release();		
			} 
			catch (FrameRecorder.Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		recorder = null;		
	}
	
	private void setUpStream(Image image) throws FrameRecorder.Exception
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
		recorder.setSampleRate(10);
		recorder.setFormat("h264");
		
		try
		{
			recorder.start();
		}
		catch (FrameRecorder.Exception e)
		{
			recorder = null;
			throw e;
		}
			
        System.out.println("Streaming server is running");
	}	
	
	public void write(Image image) throws IOException
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
			recorder.record(IplImage.createFrom(image.getImage()));
		}
		catch (Exception e)
		{
			System.out.println("Error while recording frame: " + e.getMessage());
		}
	}
}

class VideoStreamConnection implements IAccessConnectionListener<ByteBuffer>
{
	private NioTcpConnection controlConnection;
	private VideoStream		 videoStream;
	private Object			 syncObj = new Object();
	private VideoStreamProtocolHandler handler;
	
	public VideoStreamConnection(VideoStreamProtocolHandler handler, SocketChannel channel, SelectorThread selector) throws IOException, ClassNotFoundException 
	{
		this.handler = handler;
		
		controlConnection = new NioTcpConnection(channel, selector, this);
		controlConnection.resumeReading();
		
		videoStream = null;
	}
	
	private void startStream(String streamUrl)
	{
		synchronized (syncObj)
		{		
			stopStream();
			videoStream = new VideoStream(streamUrl, 30.0);
			videoStream.open();
		}
	}
	
	private void stopStream()
	{
		synchronized (syncObj)
		{
			if (videoStream != null)
			{
				videoStream.close();
				videoStream = null;
			}
		}		
	}

	@Override public void process(ByteBuffer buffer) throws ClassNotFoundException 
	{
		String cmd = new String(buffer.array(), 0, buffer.position(), Charset.forName("UTF-8"));
		
		String separator = System.getProperty("line.separator");		
		if (cmd.endsWith(separator))
			cmd = cmd.substring(0, cmd.length() - separator.length());
		
		
		System.out.println(cmd);
		
		String[] lines = cmd.split(" ");
		
		if (lines.length == 2 && lines[0].equals("STREAM"))
		{
			startStream(lines[1]);
		}
		else
		if (lines.length == 1 && lines[0].equals("STOP"))
		{
			stopStream();
		}
	}

	@Override
	public void done() 
	{
		stopStream();
	}

	@Override public void socketDisconnected() 
	{
		stopStream();
	}

	@Override public void socketException(Exception ex) 
	{
		handler.LOG.error(ex.getMessage(), ex);
	}
	
	public void write(Image image) throws IOException
	{
		synchronized (syncObj)
		{
			if (videoStream != null)
				videoStream.write(image);
		}
	}
}

public class VideoStreamProtocolHandler extends AbstractProtocolHandler<Tuple<?>> implements TCPAcceptorListener 
{
	public static final String NAME = "VideoStream";
	static final Runtime RUNTIME = Runtime.getRuntime();

	Logger LOG = LoggerFactory.getLogger(VideoStreamProtocolHandler.class);

	private SelectorThread 			selector;
	private TCPAcceptor 			acceptor;
	private List<VideoStreamConnection> 	connectedStreams;

	public VideoStreamProtocolHandler() 
	{
		super();
	}

	public VideoStreamProtocolHandler(ITransportDirection direction, IAccessPattern access, IDataHandler<Tuple<?>> dataHandler, OptionMap options) throws IOException 
	{
		super(direction, access, dataHandler, options);
		
		int port = options.getInt("port", 8076);
		
		try 
		{
			selector = SelectorThread.getInstance();
			acceptor = new TCPAcceptor(port, selector, this);
		} 
		catch (IOException e) 
		{
			selector = null;
			acceptor = null;
			
			throw e;
		}
		
		connectedStreams = new LinkedList<VideoStreamConnection>();	
	}
	
	@Override public void open() throws UnknownHostException, IOException 
	{
		acceptor.open();
	}
	
	@Override
	public void close() throws IOException 
	{
		acceptor.close();
		
		connectedStreams.clear();
	}	
	
	@Override
	public void socketConnected(AcceptorSelectorHandler acceptor, SocketChannel channel) 
	{
		try 
		{
			channel.socket().setReceiveBufferSize(10240);
			channel.socket().setSendBufferSize(10240);
			
			connectedStreams.add(new VideoStreamConnection(this, channel, selector));			
		} 
		catch (IOException | ClassNotFoundException e) 
		{
			LOG.error(e.getMessage(), e);
		}
	}

	@Override
	public void socketError(AcceptorSelectorHandler acceptor, Exception ex) 
	{
		LOG.error(ex.getMessage(), ex);
	}	
	
	@Override
	public void write(Tuple<?> object) throws IOException 
	{
		Image image = (Image) object.getAttribute(0);

		for (VideoStreamConnection stream : connectedStreams)
			stream.write(image);
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
