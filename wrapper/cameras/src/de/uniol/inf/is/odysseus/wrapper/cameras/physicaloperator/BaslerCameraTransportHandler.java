package de.uniol.inf.is.odysseus.wrapper.cameras.physicaloperator;

import static org.bytedeco.javacpp.opencv_core.*;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractSimplePullTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
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
			
//			instanceCount--;
//			if (instanceCount == 0)
				BaslerCamera.shutDownSystem();						
		}
	}

	public static ImageJCV createFromBuffer(intArray buffer, int width, int height)
	{
		int channels = 4;
		
		IplImage img = cvCreateImage(cvSize(width, height), IPL_DEPTH_8U, channels);
		ByteBuffer buf = img.getByteBuffer();
		
		int wfill = img.widthStep() - width*channels;
		
		int src=0;
		int dst=0;
		for(int y=0; y<height; y++)
		{
            for(int x=0; x<width; x++)
            {
            	buf.putInt(dst, buffer.getitem(src++));            	
            	dst += channels;
            }
            
            dst += wfill;
		}		
				
		return new ImageJCV(img);		
	}	
	
	private long lastTime = 0;
	
	@Override public Tuple<?> getNext() 
	{
		long now = System.nanoTime();
		double dt = (now - lastTime) / 1.0e9;
		System.out.println("getNext " + now / 1.0e9 + ", dt = " + dt + " = " + 1.0/dt + " FPS");
	
		lastTime = now;
		
		ImageJCV image = createFromBuffer(imageData, getImageWidth(), getImageHeight());
			
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
			boolean success = cameraCapture.grabRGB8(1000);		
			
			if (success)
				cameraCapture.getImageData(imageData.cast());
			
			return success;
		}
	}
		
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
