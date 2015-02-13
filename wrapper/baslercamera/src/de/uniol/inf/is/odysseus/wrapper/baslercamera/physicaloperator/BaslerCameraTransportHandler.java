package de.uniol.inf.is.odysseus.wrapper.baslercamera.physicaloperator;

import static org.bytedeco.javacpp.opencv_core.*;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractSimplePullTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.wrapper.baslercamera.swig.BaslerCamera;

public class BaslerCameraTransportHandler extends AbstractSimplePullTransportHandler<Tuple<IMetaAttribute>> 
{
	@SuppressWarnings("unused")
	private final Logger logger = LoggerFactory.getLogger(BaslerCameraTransportHandler.class);
	private final Object processLock = new Object();

	private String serialNumber;
	private BaslerCamera cameraCapture;
	
	Tuple<IMetaAttribute> currentTuple;
	
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
		
		serialNumber = options.get("serialnumber", "");
	}
	

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) 
	{
		return new BaslerCameraTransportHandler(protocolHandler, options);
	}


	@Override public String getName() { return "BaslerCamera"; }

	@Override public void processInOpen() throws IOException 
	{
		synchronized (processLock)
		{
			try
			{
		 		cameraCapture = new BaslerCamera(serialNumber);
				cameraCapture.start();
				currentTuple = null;
			}
			catch (RuntimeException e) 
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
			cameraCapture.stop();
			cameraCapture = null;
		}
	}

	private long lastTime = 0;
	
	@Override public Tuple<IMetaAttribute> getNext() 
	{
		long now = System.nanoTime();
		double dt = (now - lastTime) / 1.0e9;
//		System.out.println("getNext " + now / 1.0e9 + ", dt = " + dt + " = " + 1.0/dt + " FPS");
		System.out.println(serialNumber + ": " +  1.0/dt + " FPS");
		lastTime = now;

		Tuple<IMetaAttribute> tuple = currentTuple;
		currentTuple = null;		
		
        return tuple;					
	}
    
	@Override public boolean hasNext() 
	{
		synchronized (processLock)
		{
			if (cameraCapture == null) return false;
			
//			IplImage img = null;
			
			IplImage img = cvCreateImage(cvSize(cameraCapture.getImageWidth(), cameraCapture.getImageHeight()), IPL_DEPTH_8U, cameraCapture.getImageChannels());			
			ByteBuffer imageData = img.getByteBuffer();
			
			// Is it possible for an IplImage to be backed by a non-direct byte buffer?
			assert(imageData.isDirect());

			if (!cameraCapture.grabRGB8(imageData, 1000))
			{
				return false;
			}
			else
			{
				// FIX: Without this, the finalization method of the ImageJCV will not be called... ?!?
				img.getBufferedImage();
				
				currentTuple = new Tuple<IMetaAttribute>(getSchema().size(), true);
				int[] attrs = getSchema().getSDFDatatypeAttributePositions(SDFImageJCVDatatype.IMAGEJCV);
				if (attrs.length > 0) currentTuple.setAttribute(attrs[0], new ImageJCV(img));
				
				return true;				
			}
		}
	}
		
    @Override
    public boolean isSemanticallyEqualImpl(ITransportHandler o) {
    	if(!(o instanceof BaslerCameraTransportHandler)) {
    		return false;
    	}
    	BaslerCameraTransportHandler other = (BaslerCameraTransportHandler)o;
    	if(!this.serialNumber.equals(other.serialNumber))
    		return false;
    	
    	return true;
    }
}
