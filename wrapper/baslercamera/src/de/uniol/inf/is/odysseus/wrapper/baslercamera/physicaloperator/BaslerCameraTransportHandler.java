package de.uniol.inf.is.odysseus.wrapper.baslercamera.physicaloperator;

import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;

import java.io.IOException;

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
	
	private Tuple<IMetaAttribute> currentTuple;
	private ImageJCV imageJCV;
	
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
				
				imageJCV = new ImageJCV(cameraCapture.getImageWidth(), cameraCapture.getImageHeight(), IPL_DEPTH_8U, cameraCapture.getImageChannels());
				
				currentTuple = null;
			}
			catch (RuntimeException e) 
			{
				try
				{
					processInClose();
				}
				catch (Exception e2)
				{
					e2.printStackTrace();
				}
				throw new IOException(e);
			}
		}
	}
	
	@Override public void processInClose() throws IOException 
	{
		synchronized (processLock)
		{
			System.out.println("Stopping...");
			if (cameraCapture != null)
			{
				cameraCapture.stop();
				cameraCapture = null;
			}
			
			imageJCV = null;
			currentTuple = null;
			System.out.println("Stopped");
		}
	}

	private double smoothFPS = 0.0f;
	private double alpha = 0.95f;
	
	private long lastTime = 0;
	
	private int imageCount = 0;
	
	@Override public Tuple<IMetaAttribute> getNext() 
	{
		long now = System.nanoTime();
		double dt = (now - lastTime) / 1.0e9;
		double fps = 1.0/dt;

		smoothFPS = alpha*smoothFPS + (1.0-alpha)*fps; 
		
//		System.out.println("getNext " + now / 1.0e9 + ", dt = " + dt + " = " + 1.0/dt + " FPS");
		System.out.println(String.format("%d %s: %.4f FPS (%.4f)", imageCount++, serialNumber, smoothFPS, fps));
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

			if (!cameraCapture.grabRGB8(imageJCV.getImageData(), imageJCV.getImage().widthStep(), 1000))
				return false;

//			System.out.println("Frame grabbed from " + serialNumber);
				
			currentTuple = new Tuple<IMetaAttribute>(getSchema().size(), false);
			int[] attrs = getSchema().getSDFDatatypeAttributePositions(SDFImageJCVDatatype.IMAGEJCV);
			if (attrs.length > 0)
			{
				currentTuple.setAttribute(attrs[0], imageJCV);				
			}
				
			return true;				
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
