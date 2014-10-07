package de.uniol.inf.is.odysseus.wrapper.cameras.physicaloperator;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractSimplePullTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.wrapper.cameras.swig.OptrisCamera;
import de.uniol.inf.is.odysseus.wrapper.cameras.swig.intArray;

public class OptrisCameraTransportHandler extends AbstractSimplePullTransportHandler<Tuple<?>> 
{
	@SuppressWarnings("unused")
	private final Logger logger = LoggerFactory.getLogger(OptrisCameraTransportHandler.class);
	private final Object processLock = new Object();

	private String 			ethernetAddress;
	private OptrisCamera 	cameraCapture;
	
	private intArray 		imageData; 	
	
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

	public int getImageWidth() { return cameraCapture.getImageWidth(); }
	public int getImageHeight() { return cameraCapture.getImageHeight(); }
	public int getImageNumPixels() { return getImageWidth() * getImageHeight(); }	
	
	@Override public void processInOpen() throws IOException 
	{
		synchronized (processLock)
		{
			try
			{
		 		cameraCapture = new OptrisCamera(ethernetAddress);
				cameraCapture.start();
			}
			catch (RuntimeException e) 
			{
				cameraCapture = null;
				throw new IOException(e.getMessage());
			}
			
	        imageData = new intArray(getImageNumPixels());
	        
	        System.out.println("processInOpen");
		}
	}
	
	@Override public void processInClose() throws IOException 
	{
		synchronized (processLock)
		{
			System.out.println("processInClose");
			
			cameraCapture.stop();
			cameraCapture = null;
			
			imageData = null;
		}
	}

	@Override public Tuple<?> getNext() 
	{
		ImageJCV image = BaslerCameraTransportHandler.createFromBuffer(imageData, getImageWidth(), getImageHeight());
			
		@SuppressWarnings("rawtypes")
		Tuple<?> tuple = new Tuple(1, false);
        tuple.setAttribute(0, image);
        return tuple;					
	}
    
	@Override public boolean hasNext() 
	{
		synchronized (processLock)
		{
			if (cameraCapture == null) return false;		
			boolean success = true;/*cameraCapture.grabRGB8(1000);		
			
			if (success)
				cameraCapture.getImageData(imageData.cast());*/
			
			return success;
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
}
