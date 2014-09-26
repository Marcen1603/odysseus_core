package de.uniol.inf.is.odysseus.wrapper.cameras.physicaloperator;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.image.common.datatype.Image;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractSimplePullTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.wrapper.cameras.swig.BaslerCamera;
import de.uniol.inf.is.odysseus.wrapper.cameras.swig.intArray;

public class BaslerCameraTransportHandler extends AbstractSimplePullTransportHandler<Tuple<?>> 
{
	@SuppressWarnings("unused")
	private final Logger logger = LoggerFactory.getLogger(BaslerCameraTransportHandler.class);
	private final Object processLock = new Object();

	private String ethernetAddress;
	private BaslerCamera cameraCapture;
	
	private intArray 		imageData;	
	private BufferedImage 	bufferedImage;
	private int[] 			bufferedImagePixels;
	
	public BaslerCameraTransportHandler() 
	{
		super();
	}
	
	/**
	 * @param protocolHandler
	 */
	public BaslerCameraTransportHandler(final IProtocolHandler<?> protocolHandler, OptionMap options) 
	{
		super(protocolHandler, options);
		
		ethernetAddress = options.get("ethernetaddress", "");
	}
	

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) 
	{
		return new BaslerCameraTransportHandler(protocolHandler, options);
	}


	@Override public String getName() { return "BaslerCamera"; }

	private int getImageWidth() { return cameraCapture.getImageWidth(); }
	private int getImageHeight() { return cameraCapture.getImageHeight(); }
	private int getImageNumPixels() { return getImageWidth() * getImageHeight(); }	
	
	@Override public void processInOpen() throws IOException 
	{
		synchronized (processLock)
		{
			try
			{
				// TODO: Count active instances of basler cameras
//				if (instanceCount == 0)
					BaslerCamera.initializeSystem();
//				instanceCount++;
				
		 		cameraCapture = new BaslerCamera(ethernetAddress);
				cameraCapture.start();
			}
			catch (RuntimeException e) 
			{
				cameraCapture = null;
				throw new IOException(e.getMessage());
			}
			
	        imageData = new intArray(getImageNumPixels());
	        
	        bufferedImage = new BufferedImage(getImageWidth(), getImageHeight(), BufferedImage.TYPE_INT_BGR);                                
	        WritableRaster raster = bufferedImage.getRaster();
	        bufferedImagePixels = ( (DataBufferInt) raster.getDataBuffer()).getData();
	        
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
			
//			instanceCount--;
//			if (instanceCount == 0)
				BaslerCamera.shutDownSystem();						
		}
	}

	private long lastTime = 0;
	
	@Override public Tuple<?> getNext() 
	{
		synchronized (processLock)
		{
			if (cameraCapture == null) return null;		
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
	        
			Image image = new Image(Image.deepCopy(bufferedImage));
			
			@SuppressWarnings("rawtypes")
			Tuple<?> tuple = new Tuple(1, false);
	        tuple.setAttribute(0, image);
	        return tuple;					
		}
	}
    
	@Override public boolean hasNext() { return true; }
		
    @Override
    public boolean isSemanticallyEqualImpl(ITransportHandler o) {
    	if(!(o instanceof BaslerCameraTransportHandler)) {
    		return false;
    	}
    	BaslerCameraTransportHandler other = (BaslerCameraTransportHandler)o;
    	if(!this.ethernetAddress.equals(other.ethernetAddress))
    		return false;
    	
    	return true;
    }
}
