package de.uniol.inf.is.odysseus.wrapper.cameras.physicaloperator;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractSimplePullTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.wrapper.cameras.datatype.BaseImage;
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
	private BufferedImage 	bufferedImage;
	private int[] 			bufferedImagePixels;
	
	public OptrisCameraTransportHandler() 
	{
		super();
	}
	
	/**
	 * @param protocolHandler
	 */
	public OptrisCameraTransportHandler(final IProtocolHandler<?> protocolHandler, OptionMap options) {
		super(protocolHandler, options);
	}
	

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) 
	{
		final OptrisCameraTransportHandler handler = new OptrisCameraTransportHandler(protocolHandler, options);
		handler.init(options);
		return handler;
	}

	private void init(final OptionMap options) 
	{
		System.out.println("init");
		
		if (options.containsKey("ethernetaddress")) 
			ethernetAddress = options.get("ethernetaddress");
		else
			ethernetAddress = "";
	}


	@Override public String getName() { return "OptrisCamera"; }

	private int getImageWidth() { return cameraCapture.getImageWidth(); }
	private int getImageHeight() { return cameraCapture.getImageHeight(); }
	private int getImageNumPixels() { return getImageWidth() * getImageHeight(); }	
	
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
			
/*	        imageData = new intArray(getImageNumPixels());
	        
	        bufferedImage = new BufferedImage(getImageWidth(), getImageHeight(), BufferedImage.TYPE_INT_BGR);                                
	        WritableRaster raster = bufferedImage.getRaster();
	        bufferedImagePixels = ( (DataBufferInt) raster.getDataBuffer()).getData();*/
	        
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
			bufferedImage = null;
			bufferedImagePixels = null;			
		}
	}

	private long lastTime = 0;
	
	@Override public Tuple<?> getNext() 
	{
		synchronized (processLock)
		{
/*			if (cameraCapture == null) return null;		
			boolean success = cameraCapture.grabRGB8(1000);		
			if (!success)
			{
				// TODO: If an error occurs, the last image will be returned. Do something else here
				System.out.println("success == false!");//, return null!");
//				return null;
			}
			else
			{
				cameraCapture.getImageData(imageData.cast());
				
				long now = System.nanoTime();
				double dt = (now - lastTime) / 1.0e9;
				System.out.println("getNext " + now / 1.0e9 + ", dt = " + dt + " = " + 1.0/dt + " FPS");
	
				lastTime = now;
				
				int num = getImageNumPixels();
		        for (int i = 0; i < num; i++) 
		        {
		        	bufferedImagePixels[i] = imageData.getitem(i);
		        }
			}
	        
			BaseImage image = BaseImage.createNewFrom(bufferedImage);
			
			Tuple<?> tuple = new Tuple(1, false);
	        tuple.setAttribute(0, image);
	        return tuple;*/					
		}
		
		return null;
	}
    
	@Override public boolean hasNext() { return true; }
		
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
