package de.uniol.inf.is.odysseus.wrapper.optriscamera.physicaloperator;

import static org.bytedeco.javacpp.opencv_core.*;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPushTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.wrapper.optriscamera.swig.FrameCallback;
import de.uniol.inf.is.odysseus.wrapper.optriscamera.swig.OptrisCamera;

public class OptrisCameraTransportHandler extends AbstractPushTransportHandler 
{
	@SuppressWarnings("unused")
	private final Logger logger = LoggerFactory.getLogger(OptrisCameraTransportHandler.class);
	private final Object processLock = new Object();

	private String 			ethernetAddress;
	private OptrisCamera 	cameraCapture;
	
	private FrameCallback 	callback;
	private ByteBuffer		frameBuffer;
		
	public OptrisCameraTransportHandler() 
	{
		super();
	}
	
	/**
	 * @param protocolHandler
	 */
	public OptrisCameraTransportHandler(final IProtocolHandler<?> protocolHandler, OptionMap options) 
	{
		super(protocolHandler, options);
		
		ethernetAddress = options.get("ethernetaddress", "");
	}
	
	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) 
	{
		return new OptrisCameraTransportHandler(protocolHandler, options);
	}

	@Override public String getName() { return "OptrisCamera"; }

	@Override public void processInOpen() throws IOException 
	{
//		throw new UnsupportedOperationException("Operator can not be run in pull mode");
		
		synchronized (processLock)
		{
			try
			{
				callback = new FrameCallback()
		 		{
					@Override public void onFrameInit(int width, int height, int bufferSize)
					{
						frameBuffer = ByteBuffer.allocateDirect(bufferSize);
						cameraCapture.setFrameBuffer(frameBuffer);
					}
				
					@Override public void onNewFrame()
					{
						onNewFrame();
					}
		 		};
				
		 		cameraCapture = new OptrisCamera("", ethernetAddress);
			 	cameraCapture.setFrameCallback(callback);
				cameraCapture.start();
							
			}
			catch (RuntimeException e) 
			{
				cameraCapture = null;
				throw new IOException(e.getMessage());
			}
		}
		
		fireOnConnect();
	}

	protected void onNewFrame() 
	{
		synchronized (processLock)
		{		
			System.out.println("Frame received in java: ");// + frameBuffer.asShortBuffer().get(0));
			
/*			IplImage img = cvCreateImageHeader(cvSize(cameraCapture.getImageWidth(), cameraCapture.getImageHeight()), IPL_DEPTH_16S, 1);
			img.imageData(new BytePointer(buffer));
			
			@SuppressWarnings("rawtypes")
			Tuple<?> tuple = new Tuple(1, false);
	        tuple.setAttribute(0, new ImageJCV(img));
			
			fireProcess(tuple);*/
		}
	}
	
	@Override public void processInClose() throws IOException 
	{
		synchronized (processLock)
		{
			fireOnDisconnect();
			
			cameraCapture.stop();
			cameraCapture = null;
			
			callback = null;
			frameBuffer = null;
		}
	}

    @Override
    public boolean isSemanticallyEqualImpl(ITransportHandler o) {
    	if(!(o instanceof OptrisCameraTransportHandler)) {
    		return false;
    	}
    	OptrisCameraTransportHandler other = (OptrisCameraTransportHandler)o;
    	if(!this.ethernetAddress.equals(other.ethernetAddress))
    		return false;
    	
    	return true;
    }

	@Override
	public void processOutOpen() throws IOException 
	{
		throw new UnsupportedOperationException("Operator can not be used as sink");
	}

	@Override
	public void processOutClose() throws IOException 
	{
		throw new UnsupportedOperationException("Operator can not be used as sink");
	}

	@Override
	public void send(byte[] message) throws IOException 
	{
		throw new UnsupportedOperationException("Operator can not be used as sink");
	}
}
