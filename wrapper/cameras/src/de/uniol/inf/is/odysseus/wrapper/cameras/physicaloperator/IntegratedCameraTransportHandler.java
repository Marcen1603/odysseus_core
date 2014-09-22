package de.uniol.inf.is.odysseus.wrapper.cameras.physicaloperator;

import java.io.IOException;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractSimplePullTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.image.common.datatype.Image;

public class IntegratedCameraTransportHandler extends AbstractSimplePullTransportHandler<Tuple<?>> 
{
	@SuppressWarnings("unused")
	private final Logger logger = LoggerFactory.getLogger(IntegratedCameraTransportHandler.class);
	private final Object processLock = new Object();
	
	private int 				cameraId;
	private OpenCVFrameGrabber 	cameraCapture;
	
	public IntegratedCameraTransportHandler() 
	{
		super();
	}
	
	/**
	 * @param protocolHandler
	 */
	public IntegratedCameraTransportHandler(final IProtocolHandler<?> protocolHandler, OptionMap options) {
		super(protocolHandler, options);
	}
	

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) 
	{
		final IntegratedCameraTransportHandler handler = new IntegratedCameraTransportHandler(protocolHandler, options);
		handler.init(options);
		return handler;
	}

	private void init(final OptionMap options) 
	{
		System.out.println("init");
		
		if (options.containsKey("cameraid")) 
			cameraId = Integer.parseInt(options.get("cameraid"));
		else
			cameraId = 0;
	}


	@Override public String getName() { return "IntegratedCamera"; }

	@Override public void processInOpen() throws IOException 
	{
		synchronized (processLock)
		{
			cameraCapture = new OpenCVFrameGrabber(cameraId);
			try 
			{
				cameraCapture.start();
			} 
			catch (FrameGrabber.Exception e) 
			{
				cameraCapture = null;
				throw new IOException(e.getMessage());
			}
		}
	}
	
	@Override public void processInClose() throws IOException 
	{
		synchronized (processLock)
		{
			if (cameraCapture != null)
			{
				try 
				{
					cameraCapture.stop();
					cameraCapture.release();
				} 
				catch (Exception e)
				{
					throw new IOException(e.getMessage());
				}
				finally 
				{				
					cameraCapture = null;
				}
			}
		}
	}
	
	long lastTime=0;

	@Override public Tuple<?> getNext() 
	{
		IplImage iplImage = null;
		try 
		{
			synchronized (processLock)
			{
				if (cameraCapture == null) return null;
				iplImage = cameraCapture.grab();
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}		
		if (iplImage == null || iplImage.isNull()) return null;

		if (lastTime > 0)
			System.out.println("getNext dt = " + (System.nanoTime() - lastTime) / 1.0e6 + "ms");
		
		lastTime = System.nanoTime();
		
//		BaseImage image = new BaseImage(iplImage.getBufferedImage());
		Image image = new Image(iplImage.getBufferedImage());
		
		@SuppressWarnings("rawtypes")
		Tuple<?> tuple = new Tuple(1, false);
        tuple.setAttribute(0, image);
        return tuple;		
	}
    
	@Override public boolean hasNext() { return cameraCapture != null; }
		
    @Override
    public boolean isSemanticallyEqualImpl(ITransportHandler o) {
    	if(!(o instanceof IntegratedCameraTransportHandler)) {
    		return false;
    	}
    	IntegratedCameraTransportHandler other = (IntegratedCameraTransportHandler)o;
    	if(this.cameraId != other.cameraId)
    		return false;
    	
    	return true;
    }
}
